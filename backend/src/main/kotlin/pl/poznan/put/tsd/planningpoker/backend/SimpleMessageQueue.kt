package pl.poznan.put.tsd.planningpoker.backend

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.springframework.stereotype.Service

sealed class Message {
    object PlayerJoined : Message()
    object PlayerLeft : Message()
}

@Service
class SimpleMessageQueue {
    private val _queue = MutableSharedFlow<Message>()
    val queue = _queue.asSharedFlow()

    suspend fun sendPlayerJoinedMessage(): Unit = _queue.emit(Message.PlayerJoined)
    suspend fun sendPlayerLeftMessage(): Unit = _queue.emit(Message.PlayerLeft)
}