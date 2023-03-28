package pl.poznan.put.tsd.planningpoker.backend.services

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import pl.poznan.put.tsd.planningpoker.backend.components.UUIDProvider
import pl.poznan.put.tsd.planningpoker.backend.model.GameNotFoundException
import pl.poznan.put.tsd.planningpoker.backend.model.UsernameTakenException
import java.util.UUID

@ExperimentalCoroutinesApi
class GamesServiceTest {
    private val uuidProvider = mock(UUIDProvider::class.java).apply {
        `when`(generateUUID()).thenReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
    }
    private val service = GamesService(uuidProvider)

    @Test
    fun `Given creators name when creating a table then return uuid`() = runTest {
        val creator = "username"

        val uuid = service.createGame(creator)

        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), uuid)
    }

    @Test
    fun `Given game id name when joining game then join the game`() = runTest {
        val uuid = service.createGame("username")

        val result = service.joinGame(uuid, "username2")

        assertEquals(Unit, result)
    }

    @Test
    fun `Given name existing in the game when joining the game then throw UsernameTakenException`() = runTest {
        val uuid = service.createGame("username")

        val exception = runCatching { service.joinGame(uuid, "username") }.exceptionOrNull()

        assertNotNull(exception)
        assertTrue(exception is UsernameTakenException) { "When name is taken the thrown exception should be UsernameTakenException" }
    }

    @Test
    fun `Given wrong game id when joining the game then throw GameNotFoundException`() = runTest {
        service.createGame("username")
        val uuid = UUID.fromString("a438511c-7009-41fd-bc37-beda2e32270b")

        val exception = runCatching { service.joinGame(uuid, "username2") }.exceptionOrNull()

        assertTrue(exception is GameNotFoundException) { "When name is taken the thrown exception should be UsernameTakenException" }
    }
}