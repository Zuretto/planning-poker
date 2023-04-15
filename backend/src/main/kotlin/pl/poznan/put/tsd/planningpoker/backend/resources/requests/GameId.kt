package pl.poznan.put.tsd.planningpoker.backend.resources.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class GameId(
    @JsonProperty("game_id") val gameId: UUID,
)
