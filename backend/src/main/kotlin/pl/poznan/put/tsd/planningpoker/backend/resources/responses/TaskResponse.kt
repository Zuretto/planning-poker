package pl.poznan.put.tsd.planningpoker.backend.resources.responses

import pl.poznan.put.tsd.planningpoker.backend.model.Task

data class TaskResponse(val key: String, val description: String) {
    companion object {
        fun Task.toResponseModel() : TaskResponse = TaskResponse(key, description)
    }
}