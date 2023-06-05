package pl.poznan.put.tsd.planningpoker.backend.resources.responses

import pl.poznan.put.tsd.planningpoker.backend.model.PlayerGameConnection
import pl.poznan.put.tsd.planningpoker.backend.resources.requests.Card

data class PlayerResponse(val name: String, val selectedCard: Card = Card.NONE) {
    companion object {
        fun PlayerGameConnection.toResponseModel(): PlayerResponse = PlayerResponse(player.name, selectedCard)
    }
}
