package pl.poznan.put.tsd.planningpoker.backend.components

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UUIDProvider {
    fun generateUUID(): UUID = UUID.randomUUID()
}