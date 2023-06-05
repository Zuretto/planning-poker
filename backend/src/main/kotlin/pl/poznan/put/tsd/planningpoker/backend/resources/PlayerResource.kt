package pl.poznan.put.tsd.planningpoker.backend.resources

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.GameHistoryResponse
import pl.poznan.put.tsd.planningpoker.backend.services.PlayersService

@RestController
class PlayerResource(
    private val playersService: PlayersService
) {
    @GetMapping("player/{username}/history")
    suspend fun getGamesHistory(@PathVariable username: String): ResponseEntity<List<GameHistoryResponse>> {
        val games = playersService.getPlayerHistory(username)
        return ResponseEntity(games, HttpStatus.OK)
    }
}