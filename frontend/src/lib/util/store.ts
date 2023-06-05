import { writable } from 'svelte/store'

export type UserInformation = {
    accessToken: string;
    username: string;
};

export const userInformationKey = 'storage__user__information'

const storedUserString = localStorage.getItem(userInformationKey);
const storedUser = storedUserString !== null
    ? JSON.parse(storedUserString)
    : null;

export const accountStore = writable<UserInformation>(storedUser);
accountStore.subscribe(newUserInformation =>
    localStorage.setItem(userInformationKey, JSON.stringify(newUserInformation)));
