export enum Card {
    NONE = 'NONE',
    ONE = 'ONE',
    TWO = 'TWO',
    THREE = 'THREE',
    FIVE = 'FIVE',
    EIGHT = 'EIGHT',
    THIRTEEN = 'THIRTEEN',
    TWENTY_ONE = 'TWENTY_ONE',
    THIRTY_FOUR = 'THIRTY_FOUR',
    FIFTY_FIVE = 'FIFTY_FIVE',
    EIGHTY_NINE = 'EIGHTY_NINE',
    INFINITY = 'INFINITY',
    QUESTION_MARK = 'QUESTION_MARK'
}

export const cardEnumToDisplayText: {[T in Card]: string} = {
    'NONE': 'none',
    'ONE': '1',
    'TWO': '2',
    'THREE' : '3',
    'FIVE' : '5',
    'EIGHT' : '8',
    'THIRTEEN' : '13',
    'TWENTY_ONE' : '21',
    'THIRTY_FOUR' : '34',
    'FIFTY_FIVE' : '55',
    'EIGHTY_NINE' : '89',
    'INFINITY' : '∞',
    'QUESTION_MARK' : '?',

}