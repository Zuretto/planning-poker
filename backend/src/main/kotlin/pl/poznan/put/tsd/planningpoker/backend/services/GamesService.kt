package pl.poznan.put.tsd.planningpoker.backend.services

import kotlinx.coroutines.sync.withLock
import org.springframework.stereotype.Service
import pl.poznan.put.tsd.planningpoker.backend.components.UUIDProvider
import pl.poznan.put.tsd.planningpoker.backend.model.Game
import pl.poznan.put.tsd.planningpoker.backend.model.GameNotFoundException
import pl.poznan.put.tsd.planningpoker.backend.model.Player
import pl.poznan.put.tsd.planningpoker.backend.model.PlayerDoesNotExistException
import pl.poznan.put.tsd.planningpoker.backend.model.UsernameTakenException
import pl.poznan.put.tsd.planningpoker.backend.resources.requests.Card
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class GamesService(val uuidProvider: UUIDProvider) {
    private val _games = ConcurrentHashMap<UUID, Game>()
    val games: Map<UUID, Game> = _games

    /**
     * Creates game
     * @return game's UUID
     */
    suspend fun createGame(username: String): UUID {
        val id = uuidProvider.generateUUID()
        _games[id] = Game(id, mutableMapOf(username to Player(username)))
        return id
    }

    @Throws(GameNotFoundException::class, UsernameTakenException::class)
    suspend fun joinGame(id: UUID, username: String) {
        val game = getGameByIdOrThrow(id)

        game.mutex.withLock {
            if (username in game.players) throw UsernameTakenException("Username: $username is already taken")
            game.players[username] = Player(username)
        }
    }

    @Throws(GameNotFoundException::class, PlayerDoesNotExistException::class)
    suspend fun selectCard(id: UUID, username: String, card: Card) {
        val game = getGameByIdOrThrow(id)

        game.mutex.withLock {
            val player = game.players[username] ?: throw PlayerDoesNotExistException("Player with username: $username does not exist")
            game.players[username] = player.copy(selectedCard = card)
        }
    }

    @Throws(GameNotFoundException::class)
    private fun getGameByIdOrThrow(id: UUID) =
        _games[id] ?: throw GameNotFoundException("Game with id: $id has been not found")

}
