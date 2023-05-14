import type {GameResponse, TableResponse, UserStoryResponse, ValidationError} from './api-handler.models';
import { usernameStore } from "./store";
import type { Card } from "./enums";

const baseUrl = import.meta.env.VITE_BASE_URL;
const websocketBaseUrl = import.meta.env.VITE_WEBSOCKET_BASE_URL;

export const createTable = (nickname: string): Promise<TableResponse> => {
    return fetch(`${baseUrl}/poker_api/v1/table`, {
        method: 'POST',
        body: JSON.stringify({
            username: nickname
        }),
        headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
        }
    })
        .then(response => {
            usernameStore.set(nickname);
            return response.json() as Promise<TableResponse>;
        });
};

export const joinTable = (nickname: string, gameId: string): Promise<void> => {
    return fetch(`${baseUrl}/poker_api/v1/table/${gameId}`, {
        method: 'PATCH',
        body: JSON.stringify({
            username: nickname
        }),
        headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
        }
    })
        .then(async response => {
            if (response.status === 404) {
                const gottenResponse = await response.json();
                throw gottenResponse.message;
            }
        })
        .then(() => usernameStore.set(nickname));
}

export const selectCard = (nickname: string, gameId: string, card: Card): Promise<void> => {
    return fetch(`${baseUrl}/poker_api/v1/select_card`, {
        method: 'POST',
        body: JSON.stringify({
            username: nickname,
            game_id: gameId,
            card: card
        }),
        headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
        }
    })
        .then(async response => {
            if (response.status === 404) {
                const gottenResponse = await response.json();
                throw gottenResponse.message;
            }
        })
}

export const resetCard = (gameId: string): Promise<void> => {
    return fetch(`${baseUrl}/poker_api/v1/reset_cards`, {
        method: 'POST',
        body: JSON.stringify({
            game_id: gameId,
        }),
        headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
        }
    })
        .then(async response => {
            if (response.status === 404) {
                const gottenResponse = await response.json();
                throw gottenResponse.message;
            }
        })
}

export const flipCards = (gameId: string): Promise<void> => {
    return fetch(`${baseUrl}/poker_api/v1/flip_cards`, {
        method: 'POST',
        body: JSON.stringify({
            game_id: gameId,
        }),
        headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
        }
    })
        .then(async response => {
            if (response.status === 404 || response.status === 403) {
                const gottenResponse = await response.json();
                throw gottenResponse.message;
            }
        })
}

export const nextRound = (gameId: string): Promise<void> => {
    return fetch(`${baseUrl}/poker_api/v1/next_round`, {
        method: 'POST',
        body: JSON.stringify({
            game_id: gameId,
        }),
        headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
        }
    })
        .then(async response => {
            if (response.status === 404) {
                const gottenResponse = await response.json();
                throw gottenResponse.message;
            }
        })
}

export const setUserStories = (gameId: string, userStories: UserStoryResponse[]): Promise<void> => {
    return fetch(`${baseUrl}/poker_api/v1/user_stories`, {
        method: 'PUT',
        body: JSON.stringify({
            game_id: gameId,
            user_stories: userStories,
        }),
        headers: {
            'Content-Type': 'application/json',
            Accept: 'application/json',
        }
    })
        .then(async response => {
            if (response.status === 404) {
                const gottenResponse = await response.json();
                throw gottenResponse.message;
            }
        })
}

/**
 *
 * Establishes connection with the websocket endpoint.
 *
 * @param nickname - nickname of the player
 * @param gameId - id of the game
 * @param onCleanClose - callback to clean close (e.g. game has finished, tableId is wrong). If connection was closed by user, callback isn't called.
 * @param onErrorClose - callback to an error close (e.g. connection couldn't be established)
 * @param onValidationError - callback to a validation error message
 * @param onMessage - callback to received message
 *
 * @returns closeMethod - method that closes the connection.
 */
export const establishWebsocketConnection = (
    nickname: string,
    gameId: string,
    onCleanClose: (ev: CloseEvent) => any,
    onErrorClose: (closeMessage: string) => any,
    onValidationError: (error: ValidationError) => void,
    onMessage: (gameResponse: GameResponse) => void,
): () => void => {

    let wasClosedByClient = false;

    const websocketUrl = new URL(websocketBaseUrl)
    websocketUrl.pathname = '/poker_api/v1/game'
    websocketUrl.searchParams.append('game_id', gameId);
    websocketUrl.searchParams.append('username', nickname);
    const socket = new WebSocket(websocketUrl);

    socket.onmessage = (messageEvent: MessageEvent<string>) => {
        const data: GameResponse | ValidationError = JSON.parse(messageEvent.data);
        if (data.type === 'Game') {
            onMessage(data);
        } else {
            onValidationError(data);
            // connection will be closed by the server anyway, no need to close it now.
        }
    }
    socket.onclose = (ev: CloseEvent) => {
        if (wasClosedByClient) {
            return;
        }
        if (ev.wasClean) {
            onCleanClose(ev);
        } else {
            onErrorClose(ev.reason);
        }
    }

    return () => {
        wasClosedByClient = true;
        socket.close();
    }
};
