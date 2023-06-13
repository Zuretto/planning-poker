package pl.poznan.put.tsd.planningpoker.backend.services

import kotlinx.coroutines.sync.withLock
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.poznan.put.tsd.planningpoker.backend.components.CsvParser
import pl.poznan.put.tsd.planningpoker.backend.components.UUIDProvider
import pl.poznan.put.tsd.planningpoker.backend.model.*
import pl.poznan.put.tsd.planningpoker.backend.resources.MessageHandler
import pl.poznan.put.tsd.planningpoker.backend.resources.MessageType
import pl.poznan.put.tsd.planningpoker.backend.resources.requests.Card
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.GameHistoryResponse
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.GameHistoryResponse.Companion.toGameHistoryResponse
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.GameResponse.Companion.toResponseModel
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.round

@Service
class GamesService(
    private val uuidProvider: UUIDProvider,
    @Lazy private val messageHandler: MessageHandler,
    private val csvParser: CsvParser,
    private val playersService: PlayersService
) {
    private val _games = ConcurrentHashMap<UUID, Game>()
    val games: Map<UUID, Game> = _games

    val playerGameConnections: MutableList<PlayerGameConnection> = mutableListOf()

    /**
     * Creates game
     * @return game's UUID
     */
    @Throws(PlayerDoesNotExistException::class)
    suspend fun createGame(username: String): UUID {
        val player = playersService.getPlayerForLogin(username)
        val id = uuidProvider.generateUUID()
        _games[id] = Game(
            id = id, creator = username, userStories = listOf(
                UserStory(null, "", mutableListOf(), null)
            )
        )
        playerGameConnections.add(PlayerGameConnection(player, _games[id]!!))
        return id
    }

    @Throws(GameNotFoundException::class, UsernameTakenException::class, PlayerDoesNotExistException::class)
    suspend fun joinGame(id: UUID, username: String) {
        val game = getGameByIdOrThrow(id)
        val player = playersService.getPlayerForLogin(username)

        game.mutex.withLock {
            if (username in playerGameConnections.filter { it.game.id == game.id }.map { it.player.name })
                throw UsernameTakenException("Username: $username is already taken")
            playerGameConnections.add(PlayerGameConnection(player, game))
            game.areCardsVisible = false
        }
        game.sendBroadcast()
    }

    @Throws(GameNotFoundException::class, PlayerDoesNotExistException::class)
    suspend fun selectCard(id: UUID, username: String, card: Card) {
        val game = getGameByIdOrThrow(id)

        game.mutex.withLock {
            val playerGameConnection =
                playerGameConnections.filter { it.game.id == game.id && it.player.name == username }
            if (playerGameConnection.isEmpty())
                throw PlayerDoesNotExistException("Player with username: $username does not exist")
            val index = playerGameConnections.indexOf(playerGameConnection[0])
            playerGameConnections[index].selectedCard = card
            game.areCardsVisible =
                playerGameConnections.filter { it.game.id == game.id }.all { it.selectedCard != Card.NONE }
            if (game.areCardsVisible)
                game.calculateEstimation()
        }
        game.sendBroadcast()
    }

    @Throws(GameNotFoundException::class)
    suspend fun resetCards(id: UUID) {
        val game = getGameByIdOrThrow(id)

        game.resetCards()
        game.sendBroadcast()
    }

    @Throws(GameNotFoundException::class)
    suspend fun flipCards(id: UUID) {
        val game = getGameByIdOrThrow(id)

        game.flipCards()
        game.sendBroadcast()
    }

    @Throws(GameNotFoundException::class)
    suspend fun nextRound(id: UUID) {
        getGameByIdOrThrow(id).run {
            resetCards()
            round++
            if (round == userStories.size) userStories = userStories + UserStory(
                null, "", mutableListOf(),
                null
            )
            sendBroadcast()
        }
    }

    @Throws(GameNotFoundException::class)
    suspend fun updateUserStories(id: UUID, userStories: List<UserStory>) {
        val game = getGameByIdOrThrow(id)

        game.mutex.withLock {
            game.userStories = userStories
        }
        game.sendBroadcast()
    }

    @Throws(GameNotFoundException::class, InvalidFileException::class)
    suspend fun importUserStoriesFromFile(id: UUID, csvFile: MultipartFile) {
        val game = getGameByIdOrThrow(id)

        game.mutex.withLock {
            game.userStories = csvParser.readCsv(csvFile)
        }
        game.sendBroadcast()
    }

    @Throws(GameNotFoundException::class)
    suspend fun exportUserStories(id: UUID): File {
        val game = getGameByIdOrThrow(id)
        val file: File

        if (game.areCardsVisible)
            game.calculateEstimation()
        game.mutex.withLock {
            file = csvParser.writeToCsv(game.userStories)
        }
        return file
    }

    fun Game.sendBroadcast() {
        try {
            playerGameConnections.filter { it.game.id == id }.forEach {
                with(messageHandler) {
                    it.session?.sendMessageObject(MessageType.Game, toResponseModel(gamesService))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private suspend fun Game.resetCards() = mutex.withLock {
        playerGameConnections.filter { it.game.id == id }.forEach {
            it.selectedCard = Card.NONE
        }
        areCardsVisible = false
    }

    private suspend fun Game.flipCards() = mutex.withLock {
        playerGameConnections.filter { it.game.id == id }.forEach {
            if (it.selectedCard == Card.NONE)
                it.selectedCard = Card.QUESTION_MARK
        }
        areCardsVisible = true
        calculateEstimation()
    }

    private suspend fun Game.calculateEstimation() {
        var sum = 0F
        var numberOfPlayers = 0F
        playerGameConnections.filter { it.game.id == id }.forEach {
            if (it.selectedCard != Card.QUESTION_MARK) {
                sum += it.selectedCard.toInt()
                numberOfPlayers++
            }
        }
        userStories[round].estimationAverage = if (numberOfPlayers > 0)
            round(sum / numberOfPlayers).toInt()
        else
            null
    }

    @Throws(PlayerDoesNotExistException::class)
    suspend fun getPlayerHistory(username: String): List<GameHistoryResponse> {
        val history = playerGameConnections.filter { it.player.name == username }.map { it.game }
        return history.map { it.toGameHistoryResponse(this) }
    }

    @Throws(GameNotFoundException::class)
    private fun getGameByIdOrThrow(id: UUID) =
        _games[id] ?: throw GameNotFoundException("Game with id: $id has been not found")
}
