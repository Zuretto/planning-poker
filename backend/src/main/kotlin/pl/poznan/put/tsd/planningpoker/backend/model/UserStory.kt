package pl.poznan.put.tsd.planningpoker.backend.model

data class UserStory (val key: String, val name: String, val tasks: MutableList<Task>)