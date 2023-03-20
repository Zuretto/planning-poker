package pl.poznan.put.tsd.planningpoker.backend.resources

import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID


@SpringBootTest
@AutoConfigureMockMvc
class TableResourceTest {
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun createTable() {
        val uuidResponse = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        Mockito.mockStatic(UUID::class.java).use { mocked ->
            mocked.`when`<UUID>(UUID::randomUUID).thenReturn(uuidResponse)

            mockMvc.perform(
                post("/table")
                    .contentType(APPLICATION_JSON)
                    .content("{\"username\": \"user\"}")
            ).andExpect(status().isCreated)
                .andExpect(content().string(containsString("\"id\":\"123e4567-e89b-12d3-a456-426614174000\"")))
        }
    }
}
