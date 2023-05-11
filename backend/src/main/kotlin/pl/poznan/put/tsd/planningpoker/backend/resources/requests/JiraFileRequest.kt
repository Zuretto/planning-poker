package pl.poznan.put.tsd.planningpoker.backend.resources.requests

import org.springframework.web.multipart.MultipartFile
import java.util.UUID

data class JiraFileRequest(
    val gameId: UUID,
    val csvFile: MultipartFile
)
