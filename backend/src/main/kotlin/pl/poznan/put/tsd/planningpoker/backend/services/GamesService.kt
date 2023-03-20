package pl.poznan.put.tsd.planningpoker.backend.services

import org.springframework.stereotype.Service
import pl.poznan.put.tsd.planningpoker.backend.model.Game
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class GamesService {
    private val games = ConcurrentHashMap<UUID, Game>()

    /**
     * Creates game
     * @return game's UUID
     */
    fun createGame(): UUID {
        val id = UUID.randomUUID()
        games[id] = Game(id)
        return id
    }
}
