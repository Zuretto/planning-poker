package pl.poznan.put.tsd.planningpoker.backend.resources.responses

import pl.poznan.put.tsd.planningpoker.backend.model.Game
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.PlayerResponse.Companion.toResponseModel
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.UserStoryResponse.Companion.toResponseModel
import pl.poznan.put.tsd.planningpoker.backend.services.GamesService

data class GameResponse(
    val areCardsVisible: Boolean,
    val creator: String,
    val players: List<PlayerResponse>,
    val userStories: List<UserStoryResponse>,
    val round: Int,
) {
    companion object {
        fun Game.toResponseModel(gamesService: GamesService): GameResponse = GameResponse(
            areCardsVisible = areCardsVisible,
            creator = creator,
            players = gamesService.playerGameConnections.filter { it.game.id == id }.map { it.toResponseModel() },
            userStories = userStories.map { it.toResponseModel() },
            round = round
        )
    }
}