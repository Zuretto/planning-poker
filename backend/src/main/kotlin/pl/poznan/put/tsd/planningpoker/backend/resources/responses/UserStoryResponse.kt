package pl.poznan.put.tsd.planningpoker.backend.resources.responses

import pl.poznan.put.tsd.planningpoker.backend.model.UserStory
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.TaskResponse.Companion.toResponseModel

data class UserStoryResponse (val key: String, val name: String, val tasks: List<TaskResponse> = listOf()) {
    companion object {
        fun UserStory.toResponseModel() :UserStoryResponse = UserStoryResponse(key, name,
            tasks.map { it.toResponseModel() })
    }
}