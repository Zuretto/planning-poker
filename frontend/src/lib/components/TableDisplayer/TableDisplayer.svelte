<script lang="ts">
    import { usernameStore } from "../../util/store";
    import { joinTable } from "../../util/api-handler";
    import ErrorToast from "../Toast/ErrorToast.svelte";
    import Card from "./Card.svelte";

    const numbers = [0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, "∞", "?"];

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

<ErrorToast bind:this="{toastComponent}"></ErrorToast>
{#if $usernameStore === null}
    <div class="text-column">
        <h1> Welcome to the board! </h1>
        <h3> Please enter your name below to proceed: </h3>
        <label>
            Your nickname
            <input name="username" type="text" id="nickname-input" bind:value={usernameInput}>
        </label>
        <button on:click={joinBoard}>Enter Username</button>
    </div>
{:else}



    <div class="wrapper">
        <div class="text-column">
            Your table ID is {tableId}.
        </div>

        <div class="cards">
            {#each numbers as num}
                <Card number={num}/>
            {/each}
        </div>
    </div>
{/if}

<style>
    .wrapper {
        display: flex;
        flex-direction: column;
        height: 100%;
    }

    .text-column {
        flex: 1 0 auto;
    }

    .cards {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        flex-shrink: 0;
        gap: 1rem;
    }
</style>