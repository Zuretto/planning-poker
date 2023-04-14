import { writable } from 'svelte/store'

export const usernameStore = writable<string>(null);
