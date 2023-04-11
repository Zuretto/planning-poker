package pl.poznan.put.tsd.planningpoker.backend.model

import kotlinx.coroutines.sync.Mutex
import org.springframework.web.socket.WebSocketSession
import pl.poznan.put.tsd.planningpoker.backend.resources.requests.Card
import java.util.UUID

data class Game(
    val id: UUID,
    val players: MutableMap<String, Player> = mutableMapOf(),
    val areCardsVisible: Boolean = false,
) {
    val mutex: Mutex = Mutex()
}

data class Player(val name: String, val selectedCard: Card = Card.NONE, var session: WebSocketSession? = null)