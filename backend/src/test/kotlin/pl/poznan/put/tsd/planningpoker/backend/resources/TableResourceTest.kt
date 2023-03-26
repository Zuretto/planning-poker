@file:OptIn(ExperimentalCoroutinesApi::class)

package pl.poznan.put.tsd.planningpoker.backend.resources

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import pl.poznan.put.tsd.planningpoker.backend.components.UUIDProvider
import java.util.UUID


@SpringBootTest
@AutoConfigureMockMvc
class TableResourceTest {

    @MockBean
    lateinit var uuidProvider: UUIDProvider

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun createTable() {
        val uuidResponse = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        `when`(uuidProvider.generateUUID()).thenReturn(uuidResponse)
        // language=JSON
        val username = """{"username": "user"}"""

        val result = mockMvc.post("/table") {
            contentType = APPLICATION_JSON
            content = username
        }.asyncDispatch()

        result.andExpect {
            // language=JSON
            content { json("""{"id":"123e4567-e89b-12d3-a456-426614174000"}""") }
            status { isCreated() }
        }
    }
}
