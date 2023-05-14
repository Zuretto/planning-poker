package pl.poznan.put.tsd.planningpoker.backend.model

data class UserStory (var id: Int?, val name: String, val tasks: MutableList<Task>)
