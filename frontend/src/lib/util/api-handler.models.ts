export type TableResponse = {
    id: string,
};

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

export type PlayerResponse = {
    name: string;
    selectedCard: Card;
};

export type GameResponse = {
    areCardsVisible: boolean,
    players: PlayerResponse[];
};

export type ValidationError = {
    message: string;
};