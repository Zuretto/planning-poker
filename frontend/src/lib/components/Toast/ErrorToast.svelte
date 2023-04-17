<script lang="ts">
    import { writable } from "svelte/store";
    import { fade } from 'svelte/transition'

    const notifications = writable<string[]>([]);

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
                 class="notification"
                 transition:fade>
                {notification}
            </div>
        {/each}
    </div>
{/if}

<style>
    .notification {
        background-color: red;
        padding: 30px;
    }
</style>
