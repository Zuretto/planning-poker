package pl.poznan.put.tsd.planningpoker.backend.resources

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.tsd.planningpoker.backend.resources.requests.*
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.TableCreatedResponse
import pl.poznan.put.tsd.planningpoker.backend.services.GamesService
import java.util.UUID

@RestController
class TableResource(private val gamesService: GamesService) {
    @PostMapping("table")
    suspend fun createTable(@RequestBody user: User): ResponseEntity<TableCreatedResponse> {
        val id = gamesService.createGame(user.username)
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
        gamesService.joinGame(gameId, user.username)
        return ResponseEntity(Unit, HttpStatus.OK)
    }

    /**
     * Responses:
     * 200 - OK
     * 404 - No such game
     * 404 - Player does not exist
     */
    @PostMapping("select_card")
    suspend fun selectCard(@RequestBody request: CardRequest): ResponseEntity<Unit> {
        gamesService.selectCard(request.gameId, request.username, request.card)
        return ResponseEntity(Unit, HttpStatus.OK)
    }

    /**
     * Responses:
     * 200 - OK
     * 404 - No such game
     */
    @PostMapping("reset_cards")
    suspend fun resetCards(@RequestBody gameId: GameId): ResponseEntity<Unit> {
        gamesService.resetCards(gameId.gameId)
        return ResponseEntity(Unit, HttpStatus.OK)
    }

    /**
     * Responses:
     * 200 - OK
     * 404 - No such game
     */
    @PostMapping("next_round")
    suspend fun nextRound(@RequestBody gameId: GameId): ResponseEntity<Unit> {
        gamesService.nextRound(gameId.gameId)
        return ResponseEntity(Unit, HttpStatus.OK)
    }

    /**
     * Responses:
     * 200 - OK
     * 404 - No such game
     */
    @PutMapping("user_stories")
    suspend fun updateUserStories(@RequestBody request: UserStoriesRequest) : ResponseEntity<Unit> {
        gamesService.updateUserStories(request.gameId, request.userStories)
        return ResponseEntity(Unit, HttpStatus.OK)
    }

    /**
     * Responses:
     * 200 - OK
     * 400 - Invalid file
     * 404 - No such game
     */
    @PostMapping("user_stories", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun importUserStoriesFromFile(@ModelAttribute request: JiraFileRequest) : ResponseEntity<Unit> {
        gamesService.importUserStoriesFromFile(request.gameId, request.csvFile)
        return ResponseEntity(Unit, HttpStatus.OK)
    }
}
