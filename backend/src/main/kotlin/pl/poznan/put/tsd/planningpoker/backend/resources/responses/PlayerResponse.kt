package pl.poznan.put.tsd.planningpoker.backend.resources.responses

import pl.poznan.put.tsd.planningpoker.backend.model.Player
import pl.poznan.put.tsd.planningpoker.backend.resources.requests.Card

data class PlayerResponse(val name: String, val selectedCard: Card = Card.NONE) {
    companion object {
        fun Player.toResponseModel(): PlayerResponse = PlayerResponse(name, selectedCard)
    }
}
