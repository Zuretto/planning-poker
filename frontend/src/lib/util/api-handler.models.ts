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
        areCardsVisible: boolean;
        creator: string;
        players: PlayerResponse[];
        userStories: UserStoryResponse[];
        round: number;
    };
};

export type UserStoryResponse = {
    id: number;
    name: string;
    tasks: TaskResponse[];
}

export type TaskResponse = {
    id: number;
    description: string;
}

export type ValidationError = {
    type: 'Error';
    data: {
        message: string;
    };
};