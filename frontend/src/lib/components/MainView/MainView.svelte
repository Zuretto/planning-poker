<svelte:head>
    <title>Home</title>
    <meta name="description" content="Svelte demo app"/>
</svelte:head>

<script lang="ts">
    import { accountStore } from "../../util/store";
    import CreateTable from "../CreateTable/CreateTable.svelte";
    import TablesHistory from "../TablesHistory/TablesHistory.svelte";

    let usernameInput: string;
    const loginSubmit = () => {
        if (!usernameInput || !usernameInput.trim().length) {
            return;
        }
        // TODO: call login API instead of this...
        accountStore.set({
            accessToken: '',
            username: usernameInput,
        });
    };
</script>

<section>

    {#if ($accountStore === null)}
        <h1>
            Welcome to planning poker!
        </h1>
        <!--  TODO: <LoginRegisterForm/> instead of this... -->
        <form on:submit|preventDefault={loginSubmit}>
            <input name="username" type="text" id="nickname-input" placeholder="Enter your nickname"
                   bind:value={usernameInput}><br>
            <input type="submit" value="Login">
        </form>
    {:else}
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
        flex: 0.6;
    }

    h1 {
        width: 100%;
    }

    button {
        margin: 30px;
    }
</style>
