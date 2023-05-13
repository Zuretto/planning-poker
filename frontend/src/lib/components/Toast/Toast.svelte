<script lang="ts">
    import { writable } from "svelte/store";
    import { fade } from 'svelte/transition'

    const notifications = writable<string[]>([]);

    export let isError = true;
    export const toast = (message: string): void => {
        notifications.update((state) => [message, ...state]);
        setTimeout(
            () => notifications.update((state) => {
                const [, ...tail] = state;
                return tail;
            }),
            3000
        );
    };
</script>

{#if $notifications}
    <div class="notifications">
        {#each $notifications as notification}
            <div role="alert"
                 class="{isError ? 'error' : 'notification' }"
                 transition:fade>
                {notification}
            </div>
        {/each}
    </div>
{/if}

<style>
    .notifications {
        position: fixed;
        padding: 1rem;
        top: 0;
        left: 50%;
        transform: translate(-50%, 0);
        color: white;
    }

    .error {
        background-color: red;
        padding: 30px;
        border-radius: 20px;
    }

    .notification {
        background-color: #646cff;
        padding: 30px;
        border-radius: 20px;
    }
</style>
