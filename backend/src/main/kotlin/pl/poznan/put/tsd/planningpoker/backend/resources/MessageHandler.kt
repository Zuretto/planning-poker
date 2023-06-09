package pl.poznan.put.tsd.planningpoker.backend.resources

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
import pl.poznan.put.tsd.planningpoker.backend.model.Game
import pl.poznan.put.tsd.planningpoker.backend.resources.responses.GameResponse.Companion.toResponseModel
import pl.poznan.put.tsd.planningpoker.backend.services.GamesService
import java.io.IOException
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.UUID

// https://codemwnci.medium.com/kotlin-springboot-and-websockets-276029b22482

enum class MessageType {
    Error,
    Game
}


@Component
class MessageHandler(val gamesService: GamesService) :
        TextWebSocketHandler() {

    private class Message(val type: MessageType, val data: Any)

    private sealed class ValidationError(val message: String) {
        object InvalidParameters : ValidationError("Missing parameters")
        object InvalidUUID : ValidationError("Wrong game id")
        object InvalidUsername : ValidationError("Wrong username")
    }

    private val mapper = jacksonObjectMapper()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        val gameAndPlayerEither = getGameAndPlayer(session)

        try {
            gameAndPlayerEither.fold(
                ifLeft = {
                    session.sendMessageObject(MessageType.Error, it)
                    session.close(CloseStatus.POLICY_VIOLATION)
                },
                ifRight = { (game, playerGameConnection) ->
                    playerGameConnection.session = session
                    session.sendMessageObject(MessageType.Game, game.toResponseModel(gamesService))
                }
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        super.afterConnectionClosed(session, status)
        val gameAndPlayerEither = getGameAndPlayer(session)
        gameAndPlayerEither.onRight { (_, playerGameConnection) -> playerGameConnection.session = null }
    }

    fun WebSocketSession.sendMessageObject(type: MessageType, value: Any) {
        sendMessage(TextMessage(mapper.writeValueAsString(Message(type, value))))
    }

    private fun URI?.getParameters(): Either<ValidationError, Map<String, String>> = either {
        ensureNotNull(this@getParameters) { ValidationError.InvalidParameters }
        val query = this@getParameters.query
        ensureNotNull(query) { ValidationError.InvalidParameters }

        val requiredParameters = listOf("game_id", "username")
        val parameters = query
            .split('&')
            .map { it.split("=") }
            .associate { it[0] to it[1] }
        ensureNotNull(parameters) { ValidationError.InvalidParameters }
        ensure(requiredParameters.all { parameters.containsKey(it) }) { ValidationError.InvalidParameters }
        parameters.toMutableMap().apply { this["username"] = URLDecoder.decode(this["username"], Charset.defaultCharset()) }
    }

    private fun String.convertToUUID(): Either<ValidationError, UUID> = Either
        .catch { UUID.fromString(this) }
        .mapLeft { ValidationError.InvalidUUID }

    private fun findGame(gameId: UUID): Either<ValidationError, Game> = either {
        val game = gamesService.games[gameId]
        ensureNotNull(game) { ValidationError.InvalidUUID }
        game
    }

    private fun Game.findPlayer(username: String) = either {
        val playerGameConnection = gamesService.playerGameConnections.filter { it.game.id == id && it.player.name == username }
        ensure(playerGameConnection.isNotEmpty()) { ValidationError.InvalidUsername }
        playerGameConnection[0]
    }

    private fun getGameAndPlayer(session: WebSocketSession) = either {
        val parameters = session.uri.getParameters().bind()
        val uuid = parameters.getValue("game_id").convertToUUID().bind()
        val game = findGame(uuid).bind()
        val playerGameConnection = game.findPlayer(parameters.getValue("username")).bind()
        game to playerGameConnection
    }
}

@Configuration
@EnableWebSocket
class WSConfig(val messageHandler: MessageHandler) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(messageHandler, "/game")
            .setAllowedOrigins("*")
    }
}
