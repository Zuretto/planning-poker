package pl.poznan.put.tsd.planningpoker.backend.resources.responses

import pl.poznan.put.tsd.planningpoker.backend.model.UserStory
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.TaskResponse.Companion.toResponseModel

data class UserStoryResponse (val id: Int?, val name: String, val tasks: List<TaskResponse> = listOf(),
                              val estimationAverage: Int?) {
    companion object {
        fun UserStory.toResponseModel() :UserStoryResponse = UserStoryResponse(id, name,
            tasks.map { it.toResponseModel() }, estimationAverage)
    }
}