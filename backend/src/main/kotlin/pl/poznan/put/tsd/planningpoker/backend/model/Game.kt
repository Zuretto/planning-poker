package pl.poznan.put.tsd.planningpoker.backend.model

import kotlinx.coroutines.sync.Mutex
import java.util.UUID

data class Game(
    val id: UUID,
    val creator: String,
    var areCardsVisible: Boolean = false,
    var userStories: List<UserStory>,
    var round: Int = 0,
) {
    val mutex: Mutex = Mutex()
}
