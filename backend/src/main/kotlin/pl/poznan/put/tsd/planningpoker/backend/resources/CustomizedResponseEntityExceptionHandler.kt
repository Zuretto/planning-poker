package pl.poznan.put.tsd.planningpoker.backend.resources

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pl.poznan.put.tsd.planningpoker.backend.model.*

@RestControllerAdvice
class CustomizedResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(
        exception: Exception,
        request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(exception.message.orEmpty())
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(GameNotFoundException::class)
    fun handleGameNotFoundException(
        gameNotFound: GameNotFoundException,
        request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse("Game with provided id does not exist")
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UsernameTakenException::class)
    fun handleUsernameTakenException(
        usernameTaken: UsernameTakenException,
        request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse("Username has already been taken")
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(PlayerDoesNotExistException::class)
    fun handlePlayerDoesNotExistException(
        playerDoesNotExist: PlayerDoesNotExistException,
        request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse("Player with provided username does not exist")
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(WrongPasswordException::class)
    fun handlePlayerDoesNotExistException(
        wrongPasswordException: WrongPasswordException,
        request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse("Incorrect password")
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(InvalidFileException::class)
    fun handleInvalidFileException(
        invalidFile: InvalidFileException,
        request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse("Invalid file provided")
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST)
    }
}
