import type { TableResponse } from './api-handler.models';
import { usernameStore } from "./store";

const baseUrl = import.meta.env.VITE_BASE_URL as string;

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
