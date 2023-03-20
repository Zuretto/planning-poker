<svelte:head>
    <title>About</title>
    <meta name="description" content="About this app"/>
</svelte:head>

<script lang="ts">
    const createBoard = (): void => {
        // TODO
    }
</script>

<div class="text-column">
    <label>
        Your nickname
        <input name="nickname" type="text">
    </label>
    <button on:click={createBoard}>Create</button>
</div>
