package pl.poznan.put.tsd.planningpoker.backend.resources

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.TableCreatedResponse
import pl.poznan.put.tsd.planningpoker.backend.services.GamesService

@RestController
class TableResource constructor(private val games: GamesService) {
    @PostMapping("table")
    fun createTable(@RequestBody username: String): ResponseEntity<TableCreatedResponse> {
        val id = games.createGame()
        return ResponseEntity(TableCreatedResponse(id), HttpStatus.CREATED)
    }
}
