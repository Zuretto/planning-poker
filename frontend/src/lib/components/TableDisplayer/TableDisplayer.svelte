<script lang="ts">
    import { usernameStore } from "../../util/store";
    import { joinTable } from "../../util/api-handler";
    import ErrorToast from "../Toast/ErrorToast.svelte";
    import { Card } from "../../util/enums.js";
    import TableView from "../TableView/TableView.svelte";
    import SelectCard from "./SelectCard.svelte";

    const cards: Card[] = (Object.values(Card) as Card[])
        .filter(card => card !== Card.NONE);

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
            <TableView username="{$usernameStore}"
                       tableId="{tableId}"/>
        </div>
        <div class="cards">
            {#each cards as card}
                <SelectCard card={card}/>
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