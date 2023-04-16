package pl.poznan.put.tsd.planningpoker.backend.services

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import pl.poznan.put.tsd.planningpoker.backend.components.UUIDProvider
import pl.poznan.put.tsd.planningpoker.backend.model.GameNotFoundException
import pl.poznan.put.tsd.planningpoker.backend.model.PlayerDoesNotExistException
import pl.poznan.put.tsd.planningpoker.backend.model.UsernameTakenException
import pl.poznan.put.tsd.planningpoker.backend.resources.MessageHandler
import pl.poznan.put.tsd.planningpoker.backend.resources.requests.Card
import java.util.UUID

@ExperimentalCoroutinesApi
class GamesServiceTest {
    private val uuidProvider = mock(UUIDProvider::class.java).apply {
        `when`(generateUUID()).thenReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
    }
    private val messageHandler = mock(MessageHandler::class.java) // TODO test properly...
    private val service = GamesService(uuidProvider, messageHandler)

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

        val result = runCatching { service.joinGame(uuid, "username") }

        result
            .onSuccess { fail() }
            .onFailure { assertEquals(UsernameTakenException::class, it::class) }
    }

    @Test
    fun `Given wrong game id when joining the game then throw GameNotFoundException`() = runTest {
        service.createGame("username")
        val uuid = UUID.fromString("a438511c-7009-41fd-bc37-beda2e32270b")

        assertThrows<GameNotFoundException> { service.joinGame(uuid, "username2") }
    }


    @Test
    fun `Given card name when selecting the card then update player state`() = runTest {
        val uuid = service.createGame("username")
        assertEquals(Card.NONE, service.games[uuid]?.players?.get("username")?.selectedCard)

        service.selectCard(uuid, "username", Card.EIGHT)

        assertEquals(Card.EIGHT, service.games[uuid]?.players?.get("username")?.selectedCard)
    }

    @Test
    fun `Given wrong game id when selecting the card then throw GameNotFoundException`() = runTest {
        service.createGame("username")
        val uuid = UUID.fromString("a438511c-7009-41fd-bc37-beda2e32270b")

        val result = runCatching { service.selectCard(uuid, "username", Card.EIGHT) }

        result
            .onSuccess { fail() }
            .onFailure { assertEquals(GameNotFoundException::class, it::class) }
    }

    @Test
    fun `Given wrong username when selecting the card then throw PlayerDoesNotExistException`() = runTest {
        val uuid = service.createGame("username")

        val result = runCatching { service.selectCard(uuid, "username2", Card.EIGHT) }

        result
            .onSuccess { fail() }
            .onFailure { assertEquals(PlayerDoesNotExistException::class, it::class) }
    }

    @Test
    fun `Given 2 users in a game when only one selects their card then should not set Game#areCardsVisible to true`() = runTest {
        val uuid = service.createGame("username")
        service.joinGame(uuid, "username2")

        service.selectCard(uuid, "username", Card.EIGHT)

        assertFalse(service.games[uuid]!!.areCardsVisible)
    }

    @Test
    fun `Given 2 users in a game when both select their cards then should set Game#areCardsVisible to true`() = runTest {
        val uuid = service.createGame("username")
        service.joinGame(uuid, "username2")

        service.selectCard(uuid, "username", Card.EIGHT)
        service.selectCard(uuid, "username2", Card.EIGHT)

        assertTrue(service.games[uuid]!!.areCardsVisible)
    }
}