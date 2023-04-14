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

<style>
    button {
        border-radius: 8px;
        border: 1px solid transparent;
        padding: 0.6em 1.2em;
        font-size: 1em;
        font-weight: 500;
        font-family: inherit;
        cursor: pointer;
        transition: border-color 0.25s;
    }

    button:hover {
        border-color: #646cff;
    }

    button:focus,
    button:focus-visible {
        outline: 4px auto -webkit-focus-ring-color;
    }
</style>