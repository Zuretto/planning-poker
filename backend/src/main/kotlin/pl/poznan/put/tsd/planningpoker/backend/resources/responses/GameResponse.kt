package pl.poznan.put.tsd.planningpoker.backend.resources.responses

import pl.poznan.put.tsd.planningpoker.backend.model.Game
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.PlayerResponse.Companion.toResponseModel
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.UserStoryResponse.Companion.toResponseModel

data class GameResponse(
    val areCardsVisible: Boolean,
    val players: List<PlayerResponse>,
    val userStories: List<UserStoryResponse>
) {
    companion object {
        fun Game.toResponseModel(): GameResponse =
            GameResponse(areCardsVisible, players.values.map { it.toResponseModel() },
                userStories.map { it.toResponseModel() })
    }
}