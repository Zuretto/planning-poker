<script lang="ts">
    import { usernameStore } from "../../util/store";
    import { joinTable } from "../../util/api-handler";
    import ErrorToast from "../Toast/ErrorToast.svelte";

    export let tableId: string;

    let toastComponent;
    let usernameInput: string;

    const joinBoard = () => {
        if (!usernameInput || !usernameInput.trim().length) {
            return;
        }
        joinTable(usernameInput, tableId)
            .catch(errorMessage => toastComponent.toast(errorMessage));
    };
</script>

<div class="text-column">
    <ErrorToast bind:this="{toastComponent}"></ErrorToast>
    {#if $usernameStore === null}
        <h1> Welcome to the board! </h1>
        <h3> Please enter your name below to proceed: </h3>
        <label>
            Your nickname
            <input name="username" type="text" id="nickname-input" bind:value={usernameInput}>
        </label>
        <button on:click={joinBoard}>Enter Username</button>
    {:else}
        <!-- TODO display cards, show the game -->
        Here will be the game board implemented!
        <!-- <GameBoard username={$usernameStore} gameId={tableId} />       -->
    {/if}
</div>