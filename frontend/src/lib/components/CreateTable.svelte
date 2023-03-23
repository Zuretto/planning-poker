<script lang="ts">
    import { goto } from '$app/navigation';
    import type {TableResponse} from "./CreateTable.models";

    let username = '';

    const createBoard = (): void => {
        if (!username || !username.trim().length) {
            return;
        }
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
        <input name="username" type="text" id="nickname-input" bind:value={username}>
    </label>
    <button on:click={createBoard}>Create board</button>
</div>
