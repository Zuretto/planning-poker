package pl.poznan.put.tsd.planningpoker.backend.model

import kotlinx.coroutines.sync.Mutex
import java.util.UUID

data class Game(
    val id: UUID,
    val players: MutableList<Player> = mutableListOf()
) {
    val mutex: Mutex = Mutex()
}

data class Player(val name: String)