<script lang="ts">
    import { establishWebsocketConnection } from "../../util/api-handler";
    import type { PlayerResponse } from "../../util/api-handler.models";
    import { onDestroy } from "svelte";
    import ReadOnlyCard from "./ReadOnlyCard.svelte";

    export let username;
    export let tableId;

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
            players = message.data.players;
            areCardsVisible = message.data.areCardsVisible;
        }
    );

    onDestroy(() => closeWebsocket());
</script>

<div class="circle-container">
    <ul>
        {#each players as player, i}
            <li style="{`transform: translate(-50%, -50%) rotate(${(i * 360) / players.length}deg) translateY(-12rem) rotate(-${(i * 360) / players.length}deg)`}">
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
        width: 24rem;
        height: 24rem;
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
</style>