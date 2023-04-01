package pl.poznan.put.tsd.planningpoker.backend.model

import kotlinx.coroutines.sync.Mutex
import pl.poznan.put.tsd.planningpoker.backend.resources.requests.Card
import java.util.UUID

data class Game(
    val id: UUID,
    val players: MutableMap<String, Player> = mutableMapOf()
) {
    val mutex: Mutex = Mutex()
}

data class Player(val name: String, val selectedCard: Card = Card.NONE)