<svelte:head>
    <title>Home</title>
    <meta name="description" content="Svelte demo app"/>
</svelte:head>

<script lang="ts">
    import { navigate } from "svelte-routing";

    const addTableButtonHandler = (): void => {
        navigate("add-table");
    };
</script>

<section>
    <h1>
        Welcome to planning poker!
    </h1>
    <button class="standard-button" on:click={addTableButtonHandler}>Create board</button>
</section>

<style>
    section {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        flex: 0.6;
    }

    h1 {
        width: 100%;
    }

    button {
        margin: 30px;
    }
</style>
