package pl.poznan.put.tsd.planningpoker.backend.services

import org.springframework.stereotype.Service
import pl.poznan.put.tsd.planningpoker.backend.model.Player
import pl.poznan.put.tsd.planningpoker.backend.model.PlayerDoesNotExistException
import pl.poznan.put.tsd.planningpoker.backend.model.UsernameTakenException
import pl.poznan.put.tsd.planningpoker.backend.model.WrongPasswordException

@Service
class PlayersService {

    private val players: MutableList<Player> = mutableListOf()

    @Throws(UsernameTakenException::class)
    suspend fun registerPlayer(username: String, password: String) {
        if (players.map { it.name }.contains(username))
            throw UsernameTakenException("Username: $username is already taken")
        else
            players.add(Player(username, password))
    }

    @Throws(PlayerDoesNotExistException::class)
    suspend fun getPlayerForLogin(login: String): Player {
        return players.find { it.name == login } ?: throw PlayerDoesNotExistException("Player with login: $login does not exist")
    }

    @Throws(PlayerDoesNotExistException::class)
    suspend fun login(login: String, password: String) {
        val player = players.find { it.name == login }
            ?: throw PlayerDoesNotExistException("Player with login: $login does not exist")
        if (player.password != password) {
            throw WrongPasswordException("Password for player: $login is incorrect")
        }
    }
}