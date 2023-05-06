package pl.poznan.put.tsd.planningpoker.backend.resources.requests

import com.fasterxml.jackson.annotation.JsonProperty
import pl.poznan.put.tsd.planningpoker.backend.model.UserStory
import java.util.UUID

data class UserStoriesRequest(
    @JsonProperty("game_id") val gameId: UUID,
    @JsonProperty("user_stories") val userStories: List<UserStory>
)