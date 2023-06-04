package pl.poznan.put.tsd.planningpoker.backend.model

import org.springframework.web.socket.WebSocketSession
import pl.poznan.put.tsd.planningpoker.backend.resources.requests.Card

data class PlayerGameConnection (val player: Player,
                                 val game: Game,
                                 var selectedCard: Card = Card.NONE,
                                 var session: WebSocketSession? = null)