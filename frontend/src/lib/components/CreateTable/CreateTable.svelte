<script lang="ts">
    import { createTable, uploadJiraCSV } from '../../util/api-handler';
    import { navigate } from "svelte-routing";
    import Toast from "../Toast/Toast.svelte";

    let username = '';
    let isFileUploadSelected = false;
    let files: File;
    let toast: (message) => void;

    const createBoard = (): void => {
        if (!username || !username.trim().length) {
            return;
        }

        createTable(username)
            .then((tableResponse) => {
                if (isFileUploadSelected) {
                    return uploadJiraCSV(tableResponse.id, files[0]).then(() => tableResponse);
                }
                return tableResponse
            })
            .then((tableResponse) => {
                navigate(`table/${tableResponse.id}`);
            })
            .catch(errorMessage => toast(errorMessage));
    };

</script>

<Toast bind:toast="{toast}"/>
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

<style lang="scss">
  input[type=file]::file-selector-button {
    border-radius: 20px;
    border: 1px solid transparent;
    padding: 0.3rem 1rem;
    font-size: 1em;
    font-weight: 450;
    font-family: inherit;
    color: white;
    background-color: #646cff;
    cursor: pointer;

    &:hover {
      transition: 0.2s;
      background-color: #4c53c7;
    }
  }
</style>