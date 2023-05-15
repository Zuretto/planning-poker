package pl.poznan.put.tsd.planningpoker.backend.resources.requests

enum class Card {
    NONE, ONE, TWO, THREE, FIVE, EIGHT, THIRTEEN, TWENTY_ONE, THIRTY_FOUR, FIFTY_FIVE, EIGHTY_NINE, INFINITY, QUESTION_MARK;

    fun toInt(): Int {
        return when (this) {
            NONE -> 0
            ONE -> 1
            TWO -> 2
            THREE -> 3
            FIVE -> 5
            EIGHT -> 8
            THIRTEEN -> 13
            TWENTY_ONE -> 21
            THIRTY_FOUR -> 34
            FIFTY_FIVE -> 55
            EIGHTY_NINE -> 89
            INFINITY -> 100
            QUESTION_MARK -> -1
        }
    }
}