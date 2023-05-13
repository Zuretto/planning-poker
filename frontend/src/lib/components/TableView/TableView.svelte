<script lang="ts">
    import { establishWebsocketConnection, nextRound, resetCard } from "../../util/api-handler";
    import type { PlayerResponse, UserStoryResponse } from "../../util/api-handler.models";
    import { onDestroy } from "svelte";
    import ReadOnlyCard from "./ReadOnlyCard.svelte";
    import Toast from "../Toast/Toast.svelte";

    export let username: string;
    export let tableId: string;
    export let resetNotifier: () => void;
    export let userStoriesNotifier: (userStories: UserStoryResponse[]) => void;
    export let roundNotifier: (round: number) => void;

    let areCardsVisible = false;
    let clickedInvite = false;
    let players: PlayerResponse[] = [];
    let toast: (message) => void;

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
            .catch(error => {/*TODO error handling*/
            });
    };

    const handleNextRound = () => {
        nextRound(tableId)
            .catch(error => {/*TODO error handling*/
            });
    };

    const handleInviteOthers = () => {
        navigator.clipboard.writeText(document.URL);
        clickedInvite = true;
        setTimeout(() => {
            clickedInvite = false;
        }, 2000);
        toast('copied link to clipboard');
    }

    onDestroy(() => closeWebsocket());
</script>

<Toast bind:toast={toast}
       isError="{false}"/>
<div class="circle-container">
    <button on:click={handleInviteOthers}
            class="button invite">
        Invite others
        {#if clickedInvite } &#10003;{/if}
    </button>
    <div class="carousel">
        <ul>
            {#each players as player, i}
                <li style="{`transform: translate(-50%, -50%) rotate(${(i * 360) / players.length}deg) translateY(-8rem) rotate(-${(i * 360) / players.length}deg)`}">
                    <ReadOnlyCard isSelectionVisible="{areCardsVisible}"
                                  player="{player}"/>
                </li>
            {/each}
        </ul>
    </div>
    <div class="button-container">
        {#if areCardsVisible}
            <button on:click={handleResetButton}
                    class="button"> Reset
            </button>
            <button on:click={handleNextRound}
                    class="next-round-button"> Next Round
            </button>
        {/if}
    </div>
</div>

<style lang="scss">
    .circle-container {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .carousel {
        flex: 1 0 auto;
    }

    .button-container {
        display: flex;
        flex-direction: row;
        align-items: baseline;
        gap: 1rem;
    }

    ul {
        list-style-type: none;
        margin: 0;
        padding: 0;
        position: relative;
        top: 50%;
        left: 50%;
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

    .button {
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
        &:hover {
            transition: 0.2s;
            background-color: #4c53c7;
        }
    }

    .next-round-button {
        @extend .button;
        border: 1px solid #646cff;
        color: #646cff;
        background-color: white;
        &:hover {
            transition: 0.2s;
            background-color: #bcbcc0;
        }
    }

    .invite {
        @extend .button;
        margin-top: 1rem;
    }
</style>
