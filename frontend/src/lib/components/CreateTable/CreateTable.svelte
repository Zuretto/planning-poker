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
    <form on:submit|preventDefault={createBoard}>
        <input name="username" type="text" id="nickname-input" placeholder="Enter your nickname"
               bind:value={username}><br>
        <input type="submit" value="Create board">
    </form>
</div>
