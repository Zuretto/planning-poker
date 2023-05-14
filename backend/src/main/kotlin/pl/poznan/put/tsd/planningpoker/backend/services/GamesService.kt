package pl.poznan.put.tsd.planningpoker.backend.services

import kotlinx.coroutines.sync.withLock
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.poznan.put.tsd.planningpoker.backend.components.UUIDProvider
import pl.poznan.put.tsd.planningpoker.backend.model.*
import pl.poznan.put.tsd.planningpoker.backend.components.CsvParser
import pl.poznan.put.tsd.planningpoker.backend.resources.MessageHandler
import pl.poznan.put.tsd.planningpoker.backend.resources.MessageType
import pl.poznan.put.tsd.planningpoker.backend.resources.requests.Card
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.GameResponse.Companion.toResponseModel
import java.io.IOException
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class GamesService(
    private val uuidProvider: UUIDProvider,
    @Lazy private val messageHandler: MessageHandler,
    private val csvParser: CsvParser
) {
    private val _games = ConcurrentHashMap<UUID, Game>()
    val games: Map<UUID, Game> = _games

    /**
     * Creates game
     * @return game's UUID
     */
    suspend fun createGame(username: String): UUID {
        val id = uuidProvider.generateUUID()
        _games[id] = Game(id = id, creator = username, players = mutableMapOf(username to Player(username)), userStories = listOf())
        return id
    }

    @Throws(GameNotFoundException::class, UsernameTakenException::class)
    suspend fun joinGame(id: UUID, username: String) {
        val game = getGameByIdOrThrow(id)

        game.mutex.withLock {
            if (username in game.players) throw UsernameTakenException("Username: $username is already taken")
            game.players[username] = Player(username)
            game.areCardsVisible = false
        }
        game.sendBroadcast()
    }

    @Throws(GameNotFoundException::class, PlayerDoesNotExistException::class)
    suspend fun selectCard(id: UUID, username: String, card: Card) {
        val game = getGameByIdOrThrow(id)

        game.mutex.withLock {
            val player = game.players[username]
                ?: throw PlayerDoesNotExistException("Player with username: $username does not exist")
            game.players[username] = player.copy(selectedCard = card)
            game.areCardsVisible = game.players.all { it.value.selectedCard != Card.NONE }
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
            if (round == userStories.size) userStories = userStories + UserStory("", "", mutableListOf())
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

    fun Game.sendBroadcast() {
        try {
            players.values.forEach {
                with(messageHandler) {
                    it.session?.sendMessageObject(MessageType.Game, toResponseModel())
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private suspend fun Game.resetCards() = mutex.withLock {
        players.entries.forEach { (key, player) ->
            players[key] = player.copy(selectedCard = Card.NONE)
        }
        areCardsVisible = false
    }

    private suspend fun Game.flipCards() = mutex.withLock {
        players.entries.forEach { (key, player) ->
            players[key] = player.copy(
                selectedCard = if (player.selectedCard == Card.NONE) Card.QUESTION_MARK else player.selectedCard
            )
        }
        areCardsVisible = true
    }

    @Throws(GameNotFoundException::class)
    private fun getGameByIdOrThrow(id: UUID) =
        _games[id] ?: throw GameNotFoundException("Game with id: $id has been not found")
}
