<svelte:head>
    <title>About</title>
    <meta name="description" content="About this app"/>
</svelte:head>

<script lang="ts">
    import { goto } from '$app/navigation';
    import type {TableResponse} from "./add-table.models";

    let username = '';

    const createBoard = (): void => {
        fetch(`http://localhost:8080/poker_api/v1/table`, {
            method: 'POST',
            body: username,
        })
            .then((response) => response.json() as TableResponse)
            .then((tableResponse) => {
                goto(`table/${tableResponse.id}`);
            });
    };
</script>

<div class="text-column">
    <label>
        Your nickname
        <input name="username" type="text" bind:value={username}>
    </label>
    <button on:click={createBoard}>Create board</button>
</div>
