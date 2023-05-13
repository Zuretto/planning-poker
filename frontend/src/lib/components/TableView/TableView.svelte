<script lang="ts">
    import { establishWebsocketConnection, resetCard } from "../../util/api-handler";
    import type { PlayerResponse } from "../../util/api-handler.models";
    import { onDestroy } from "svelte";
    import ReadOnlyCard from "./ReadOnlyCard.svelte";
    import Toast from "../Toast/Toast.svelte";

    export let username: string;
    export let tableId: string;
    export let resetNotifier: () => void;

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
            players = message.data.players;
            areCardsVisible = message.data.areCardsVisible;
        }
    );

    const handleResetButton = () => {
        resetCard(tableId)
            .catch(error => {/*TODO error handling*/
            });
    };

    const handleInviteOthers = () => {
        navigator.clipboard.writeText(document.URL);
        clickedInvite = true;
        setTimeout(() => {clickedInvite = false;}, 2000);
        toast('copied link to clipboard');
    }

    onDestroy(() => closeWebsocket());
</script>

<Toast bind:toast={toast}
       isError="{false}" />
<div class="circle-container">

    <div class="cards-list">
        <button on:click={handleInviteOthers}
                class="button invite">
            Invite others {#if clickedInvite } &#10003;{/if}
        </button>
        <ul>
            {#each players as player, i}
                <li style="{`transform: translate(-50%, -50%) rotate(${(i * 360) / players.length}deg) translateY(-8rem) rotate(-${(i * 360) / players.length}deg)`}">
                    <ReadOnlyCard isSelectionVisible="{areCardsVisible}"
                                  player="{player}"/>
                </li>
            {/each}
        </ul>
        {#if areCardsVisible}
            <button on:click={handleResetButton}
                    class="reset-button"> Reset
            </button>
        {/if}
    </div>

    <div class="user-story-view">
        <!--  TODO user story CRUD  -->
    </div>
</div>

<style lang="scss">
    .circle-container {
        display: flex;
        flex-direction: row;
        width: 100%;
        height: 100%;
    }

    .cards-list {
        flex: 1 1 auto;
        width: calc(min(calc(90rem), 100vw - 64px) / 3 * 1);
        border-radius: 20px;
        border: 1px solid transparent;
        outline: 2px solid #646cff;
    }

    .user-story-view {
        flex: 5 1 auto;
        width: calc(min(calc(90rem), 100vw - 64px) / 3 * 2);
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
    }

    .reset-button {
        @extend .button;
        position: relative;
        top: 50%;
    }

    .invite {
        @extend .button;
        margin-top: 1rem;
        &:hover {
            transition: 0.2s;
            background-color: #4c53c7;
        }
    }
</style>
