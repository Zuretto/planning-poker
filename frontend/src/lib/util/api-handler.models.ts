import type { Card } from "./enums";

export type TableResponse = {
    id: string,
};

export type PlayerResponse = {
    name: string;
    selectedCard: Card;
};

export type GameResponse = {
    type: 'Game';
    data: {
        areCardsVisible: boolean,
        players: PlayerResponse[];
    };
};

export type ValidationError = {
    type: 'Error';
    data: {
        message: string;
    };
};