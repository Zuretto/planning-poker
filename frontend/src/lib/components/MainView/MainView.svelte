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
    <button on:click={addTableButtonHandler}>Create board</button>
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
        border-radius: 20px;
        border: 1px solid transparent;
        padding: 0.6em 1.2em;
        margin: 30px;
        font-size: 1em;
        font-weight: 500;
        font-family: inherit;
        cursor: pointer;
        transition: border-color 0.25s;
        width: 250px;
        align-self: center;
    }

    button:hover {
        border-color: #646cff;
    }

    button:focus,
    button:focus-visible {
        outline: 4px auto -webkit-focus-ring-color;
    }
</style>
