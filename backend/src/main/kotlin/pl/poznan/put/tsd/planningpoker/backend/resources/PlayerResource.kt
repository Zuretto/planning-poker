package pl.poznan.put.tsd.planningpoker.backend.resources

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.tsd.planningpoker.backend.model.Player
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.GameHistoryResponse
import pl.poznan.put.tsd.planningpoker.backend.services.GamesService
import pl.poznan.put.tsd.planningpoker.backend.services.PlayersService

@RestController
class PlayerResource(
    private val playersService: PlayersService,
    private val gamesService: GamesService
) {
    @GetMapping("player/{username}/history")
    suspend fun getGamesHistory(@PathVariable username: String): ResponseEntity<List<GameHistoryResponse>> {
        val games = gamesService.getPlayerHistory(username)
        return ResponseEntity(games, HttpStatus.OK)
    }

    @PostMapping("register")
    suspend fun register(@RequestBody player: Player): ResponseEntity<Unit> {
        playersService.registerPlayer(player.name, player.password)
        return ResponseEntity(Unit, HttpStatus.OK)
    }

    @PostMapping("login")
    suspend fun login(@RequestBody player: Player): ResponseEntity<Unit> {
        playersService.login(player.name, player.password)
        return ResponseEntity(Unit, HttpStatus.OK)
    }
}