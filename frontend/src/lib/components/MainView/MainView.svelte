<svelte:head>
    <title>Home</title>
    <meta name="description" content="Svelte demo app"/>
</svelte:head>

<script lang="ts">
    import { accountStore } from "../../util/store";
    import CreateTable from "../CreateTable/CreateTable.svelte";
    import TablesHistory from "../TablesHistory/TablesHistory.svelte";
    import LoginRegister from "../CreateTable/LoginRegister.svelte";

    const logout = () => {
        accountStore.set(null);
    };
</script>

<section>

    {#if ($accountStore === null)}
        <LoginRegister/>
    {:else}
        <button class="standard-button" on:click={logout}>Log out</button>

        <h1> Dashboard </h1>

        <CreateTable/>

        <TablesHistory/>

    {/if}
</section>

<style>
    section {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        gap: 1rem;
        flex: 0.6;
    }

    h1 {
        width: 100%;
    }
</style>
