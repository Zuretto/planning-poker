<script lang="ts">
    import { Card } from "../../util/enums.js";
    import { cardEnumToDisplayText } from "../../util/enums.js";

    export let card: Card;
    export let selectedCard: Card;

</script>

<button class:selected="{selectedCard === card}"
        class:not-selected="{selectedCard !== card}"
        on:click>
            {cardEnumToDisplayText[card]}
</button>

<style>
    .not-selected {
        padding: 3rem 0;
        border-radius: 8px;
        border: 1px solid transparent;
        font-size: 2rem;
        font-weight: 500;
        font-family: inherit;
        cursor: pointer;
        transition: transform 0.25s, border-color 0.25s;
        flex: 0 0 6.25rem;
        text-align: center;
    }

    .not-selected:hover
    {
        border: 1px solid #646cff;
        outline-color: transparent;
        transition: transform 0.25s, border-color 0.25s, outline-color 0.25s;
        box-shadow: 0 0.5em 0.5em -0.4em #646cff;
        transform: translateY(-0.25em);
    }

    .selected {
        padding: 3rem 0;
        border-radius: 8px;
        border: 1px solid #646cff;
        outline: 3px solid #646cff;
        font-size: 2rem;
        font-weight: 500;
        font-family: inherit;
        cursor: pointer;
        transition: transform 0.25s, border-color 0.25s, outline-color 0.25s;
        flex: 0 0 100px;
        text-align: center;
        box-shadow: 0 0.5em 0.5em -0.4em #646cff;
        transform: translateY(-0.25em);
    }
</style>
