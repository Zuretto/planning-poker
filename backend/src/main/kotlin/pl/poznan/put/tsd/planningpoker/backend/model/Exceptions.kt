package pl.poznan.put.tsd.planningpoker.backend.model

class GameNotFoundException(message: String) : Exception(message)

class UsernameTakenException(message: String) : Exception(message)

class PlayerDoesNotExistException(message: String) : Exception(message)

class WrongPasswordException(message: String) : Exception(message)

class InvalidFileException(message: String) : Exception(message)
