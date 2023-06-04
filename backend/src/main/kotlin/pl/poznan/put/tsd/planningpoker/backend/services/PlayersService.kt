package pl.poznan.put.tsd.planningpoker.backend.services

import org.springframework.stereotype.Service
import pl.poznan.put.tsd.planningpoker.backend.model.Player
import pl.poznan.put.tsd.planningpoker.backend.model.PlayerDoesNotExistException
import pl.poznan.put.tsd.planningpoker.backend.model.UsernameTakenException
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.GameHistoryResponse
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.GameHistoryResponse.Companion.toResponseModel
import kotlin.jvm.Throws

@Service
class PlayersService(
    private val gamesService: GamesService
) {

    val players: MutableList<Player> = mutableListOf()

    @Throws(UsernameTakenException::class)
    suspend fun registerPlayer(username: String, password: String) {
        if (players.map { it.name }.contains(username))
            throw UsernameTakenException("Username: $username is already taken")
        else
            players.add(Player(username, password))
        // TODO return JWT token
    }

    @Throws(PlayerDoesNotExistException::class)
    suspend fun getPlayerHistory(username: String): List<GameHistoryResponse> {
        val history = gamesService.playerGameConnections.filter { it.player.name == username }.map { it.game }
        return history.map { it.toResponseModel(gamesService) }
    }
}