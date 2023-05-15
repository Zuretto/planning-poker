package pl.poznan.put.tsd.planningpoker.backend.model

data class Task (var id: Int?, val description: String, val parentId: Int?, val parentSummary: String?)
