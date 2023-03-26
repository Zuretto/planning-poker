package pl.poznan.put.tsd.planningpoker.backend.services

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.stereotype.Service
import pl.poznan.put.tsd.planningpoker.backend.components.UUIDProvider
import pl.poznan.put.tsd.planningpoker.backend.model.Game
import pl.poznan.put.tsd.planningpoker.backend.model.GameNotFoundException
import pl.poznan.put.tsd.planningpoker.backend.model.Player
import pl.poznan.put.tsd.planningpoker.backend.model.UsernameTakenException
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class GamesService(val uuidProvider: UUIDProvider) {
    private val games = ConcurrentHashMap<UUID, Game>()

    /**
     * Creates game
     * @return game's UUID
     */
    suspend fun createGame(username: String): UUID {
        val id = uuidProvider.generateUUID()
        games[id] = Game(id).apply { players.add(Player(username)) }
        return id
    }

    @Throws(GameNotFoundException::class, UsernameTakenException::class)
    suspend fun joinGame(id: UUID, playerName: String) {
        if (!games.containsKey(id)) throw GameNotFoundException("Game with id: $id has been not found")
        val game = games.getValue(id)

        game.mutex.lock {
            if (game.players.any { it.name == playerName }) throw UsernameTakenException("Username: $playerName is already taken")
            game.players.add(Player(playerName))
        }
    }
}
