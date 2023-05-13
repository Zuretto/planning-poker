<script lang="ts">
    import {establishWebsocketConnection, nextRound, resetCard} from "../../util/api-handler";
    import type {PlayerResponse, UserStoryResponse} from "../../util/api-handler.models";
    import { onDestroy } from "svelte";
    import ReadOnlyCard from "./ReadOnlyCard.svelte";

    export let username: string;
    export let tableId: string;
    export let resetNotifier: () => void;
    export let userStoriesNotifier: (userStories: UserStoryResponse[]) => void;
    export let roundNotifier: (round: number) => void;

    let areCardsVisible = false;
    let players: PlayerResponse[] = [];

    const closeWebsocket = establishWebsocketConnection(
        username,
        tableId,
        (closeEvent) => {
        },
        (errorCloseEvent) => {/* TODO toast message */
        },
        (validationError) => {/* TODO toast message */
        },
        (message) => {
            if (areCardsVisible && !message.data.areCardsVisible) {
                // reset has occurred
                resetNotifier();
            }
            userStoriesNotifier(message.data.userStories);
            roundNotifier(message.data.round);
            players = message.data.players;
            areCardsVisible = message.data.areCardsVisible;
        }
    );

    const handleResetButton = () => {
        resetCard(tableId)
            .catch(error => {/*TODO error handling*/});
    };

    const handleNextRound = () => {
        nextRound(tableId)
            .catch(error => {/*TODO error handling*/});
    };

    onDestroy(() => closeWebsocket());
</script>

<div class="circle-container">
    {#if areCardsVisible}
        <button on:click={handleResetButton}
                class="reset-button"> Reset
        </button>
        <button on:click={handleNextRound}
                class="next-round-button"> Next Round
        </button>
    {/if}
    <ul>
        {#each players as player, i}
            <li style="{`transform: translate(-50%, -50%) rotate(${(i * 360) / players.length}deg) translateY(-8rem) rotate(-${(i * 360) / players.length}deg)`}">
                <ReadOnlyCard isSelectionVisible="{areCardsVisible}"
                              player="{player}"/>
            </li>
        {/each}
    </ul>
</div>

<style>
    .circle-container {
        position: relative;
        display: flex;
        place-items: center;
        width: 20rem;
        height: 20rem;
        margin: 15px;
        justify-content: center;
        text-align: center;
        border-radius: 50%;
    }

    ul {
        list-style-type: none;
        margin: 0;
        padding: 0;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
    }

    ul > li {
        display: block;
        font-weight: bold;
        text-align: center;
        border-radius: 50%;
        position: absolute;
        top: 0;
        left: 0;
        transition: transform .5s cubic-bezier(0.5, 0, 0.35, 1);
        transform: translate(-50%, -50%);
        transform-origin: center center;
    }

    .reset-button {
        align-self: center;
        margin-bottom: 2rem;
        border-radius: 20px;
        border: 1px solid transparent;
        padding: 0.3rem 1rem;
        font-size: 1em;
        font-weight: 450;
        font-family: inherit;
        color: white;
        background-color: #646cff;
        cursor: pointer;
    }
    .next-round-button {
        align-self: center;
        margin-bottom: 2rem;
        margin-left: 1rem;
        border-radius: 20px;
        border: 1px solid #646cff;
        padding: 0.3rem 1rem;
        font-size: 1em;
        font-weight: 450;
        font-family: inherit;
        color: #646cff;
        background-color: white;
        cursor: pointer;
    }
</style>
