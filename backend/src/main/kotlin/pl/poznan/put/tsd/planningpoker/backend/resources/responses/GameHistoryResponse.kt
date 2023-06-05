package pl.poznan.put.tsd.planningpoker.backend.resources.responses

import pl.poznan.put.tsd.planningpoker.backend.model.Game
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.UserStoryResponse.Companion.toResponseModel
import pl.poznan.put.tsd.planningpoker.backend.services.GamesService

data class GameHistoryResponse(
    val creator: String,
    val players: List<String>,
    val userStories: List<UserStoryResponse>,
) {
    companion object {
        fun Game.toResponseModel(gamesService: GamesService): GameHistoryResponse = GameHistoryResponse(
            creator = creator,
            players = gamesService.playerGameConnections.filter { it.game.id == id }.map { it.player.name },
            userStories = userStories.map { it.toResponseModel() }
        )
    }
}