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
    <h1> Create a new board </h1>
    <h3> Please enter your name below to proceed: </h3>
    <input name="username" type="text" id="nickname-input" placeholder="Enter your nickname" bind:value={username}>
    <button on:click={createBoard}>Create board</button>
</div>

<style>
    button {
        border-radius: 20px;
        border: 1px solid transparent;
        padding: 0.6em 1.2em;
        font-size: 1em;
        font-weight: 500;
        font-family: inherit;
        cursor: pointer;
        transition: border-color 0.25s;
        width: 250px;
        align-self: center;
    }

    button:hover {
        border-color: #646cff;
    }

    button:focus,
    button:focus-visible {
        outline: 4px auto -webkit-focus-ring-color;
    }
</style>