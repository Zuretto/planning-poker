<script lang="ts">
    import { createTable } from '../../util/api-handler';
    import { navigate } from "svelte-routing";

    let username = '';
    const createBoard = (): void => {
        if (!username || !username.trim().length) {
            return;
        }

        createTable(username)
            .then((tableResponse) => {
                navigate(`table/${tableResponse.id}`);
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
