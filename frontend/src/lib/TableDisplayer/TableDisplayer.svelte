<script lang="ts">
    import Card from "./Card.svelte";

    export let tableId;
    const numbers = [0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, "∞", "?"];
</script>
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