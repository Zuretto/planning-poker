package pl.poznan.put.tsd.planningpoker.backend.resources

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.tsd.planningpoker.backend.model.User
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.TableCreatedResponse
import pl.poznan.put.tsd.planningpoker.backend.services.GamesService
import java.util.UUID

@RestController
class TableResource(private val games: GamesService) {
    @PostMapping("table")
    suspend fun createTable(@RequestBody user: User): ResponseEntity<TableCreatedResponse> {
        val id = games.createGame(user.username)
        return ResponseEntity(TableCreatedResponse(id), HttpStatus.CREATED)
    }

    /**
     * Responses:
     * 200 - OK
     * 404 - No such game
     * 409 - Username already taken
     */
    @PatchMapping("table/{gameId}")
        suspend fun joinTable(@RequestBody user: User, @PathVariable gameId: UUID): ResponseEntity<Unit> {
        games.joinGame(gameId, user.username)
        return ResponseEntity(Unit, HttpStatus.OK)
    }
}
