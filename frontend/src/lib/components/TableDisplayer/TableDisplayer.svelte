<script lang="ts">
    import { usernameStore } from "../../util/store";
    import { joinTable, selectCard } from "../../util/api-handler";
    import ErrorToast from "../Toast/ErrorToast.svelte";
    import { Card } from "../../util/enums.js";
    import TableView from "../TableView/TableView.svelte";
    import SelectCard from "./SelectCard.svelte";

    const cards: Card[] = (Object.values(Card) as Card[])
        .filter(card => card !== Card.NONE);

    export let tableId: string;
    export let selectedCard: Card = Card.NONE;

    let toast: (message) => void;
    let usernameInput: string;
    let submitted: boolean = false;
    let disabled: boolean = true;

    const joinBoard = () => {
        if (!usernameInput || !usernameInput.trim().length) {
            return;
        }
        joinTable(usernameInput, tableId)
            .catch(errorMessage => toast(errorMessage));
    };

    const submitCard = () => {
        selectCard($usernameStore, tableId, selectedCard).then(() => {
            disabled = true;
            submitted = true;
        }).catch(errorMessage => toast(errorMessage));
    }

    const onClickCard = (card: Card) => {
        if (!submitted){
            selectedCard = card;
            disabled = false;
        }
    }

</script>

<ErrorToast bind:toast="{toast}"/>
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
        <button class="submit" on:click={submitCard} {disabled}>Submit</button>
        <div class="cards">
            {#each cards as card}
                <SelectCard on:click={() => onClickCard(card)} card={card} bind:selectedCard={selectedCard}/>
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

    .submit {
        align-self: center;
        margin-bottom: 2rem;
        border-radius: 8px;
        border: 1px solid transparent;
        padding: 0.6em 1.2em;
        font-size: 1em;
        font-weight: 500;
        font-family: inherit;
        color: white;
        background-color: #646cff;
        cursor: pointer;
        transition: border-color 0.25s, background-color 0.25s;
        width: 150px;
    }

    .submit:hover {
        border-color: #646cff;
    }

    .submit:focus,
    .submit:focus-visible {
        outline: 4px auto -webkit-focus-ring-color;
    }
    .submit:disabled {
        background-color: #cccccc
    }
</style>