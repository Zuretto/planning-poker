import type { TableResponse } from './api-handler.models';

// @ts-ignore
const baseUrl = import.meta.env.VITE_BASE_URL;

export const createTable = (nickname: string): Promise<TableResponse> => {
    return fetch(`${baseUrl}/poker_api/v1/table`, {
        method: 'POST',
        body: nickname,
    })
        .then(response => response.json())
        .then(config => config as TableResponse);
};