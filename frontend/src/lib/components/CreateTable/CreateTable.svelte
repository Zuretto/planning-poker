<script lang="ts">
    import { createTable, uploadJiraCSV } from '../../util/api-handler';
    import { navigate } from "svelte-routing";

    let username = '';
    let isFileUploadSelected = false;
    let files: File;

    const createBoard = (): void => {
        if (!username || !username.trim().length) {
            return;
        }

        createTable(username)
            .then((tableResponse) => {
                if (isFileUploadSelected) {
                    uploadJiraCSV(tableResponse.id, files[0])
                }
                return tableResponse
            })
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
        <input type="checkbox" bind:checked={isFileUploadSelected}> Import Jira File<br>
        {#if isFileUploadSelected}
            <input type="file" accept="text/csv" bind:files><br><br>
        {/if}
        <input type="submit" value="Create board">
    </form>
</div>
