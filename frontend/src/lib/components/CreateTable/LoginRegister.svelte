<script lang="ts">
    import Toast from "../Toast/Toast.svelte";
    import {accountStore} from "../../util/store";
    import {login, register} from "../../util/api-handler";

    let toast: (message) => void;
    let username: string;
    let password: string;
    let confirmPassword: string;

    enum ActionType {
        FORM,
        LOGIN,
        REGISTER
    }

    let action = ActionType.FORM;

    const handleLogin = async () => {
        if (username === undefined || username.trim() === "") {
            toast("Please enter a username");
            return;
        }
        if (password === undefined || password.trim() === "") {
            toast("Please enter a password");
            return;
        }
        login(username, password)
            .then(() => accountStore.set({username: username}))
            .catch(errorMessage => toast(errorMessage));
    }
    const handleRegister = async () => {
        if (username === undefined || username.trim() === "") {
            toast("Please enter a username");
            return;
        }
        if (password === undefined || password.trim() === "") {
            toast("Please enter a password");
            return;
        }
        if (confirmPassword !== password) {
            toast("Passwords do not match");
            return;
        }
        register(username, password)
            .then(() => accountStore.set({username: username}))
            .catch(errorMessage => toast(errorMessage));
    }

    const toLogin = () => {
        action = ActionType.LOGIN;
    }
    const toRegister = () => {
        action = ActionType.REGISTER;
    }

    const toForm = () => {
        action = ActionType.FORM;
    }

</script>

<Toast bind:toast="{toast}"/>
<div class="text-column">
    {#if action === ActionType.FORM}
        <button class="standard-button" on:click={toLogin}>Login</button>
        <br/>
        <button class="standard-button" on:click={toRegister}>Register</button>
    {:else if action === ActionType.LOGIN}
        <h3> Please log in to proceed: </h3>
        <form on:submit|preventDefault={handleLogin}>
            <input name="username" type="text" placeholder="Enter your nickname"
                   bind:value={username}><br>
            <input name="password" type="password" placeholder="Enter your password"
                   bind:value={password}><br>
            <input type="submit" value="Log in">
        </form>
        <br/>
        <button class="standard-button" on:click={toForm}>Back</button>
    {:else if action === ActionType.REGISTER}
        <h3>Register</h3>
        <form on:submit|preventDefault={handleRegister}>
            <input name="username" type="text" placeholder="Enter your nickname"
                   bind:value={username}><br>
            <input name="password" type="password" placeholder="Enter your password"
                   bind:value={password}><br>
            <input name="password" type="password" placeholder="Confirm your password"
                   bind:value={confirmPassword}><br>
            <input type="submit" value="Register">
        </form>
        <br/>
        <button class="standard-button" on:click={toForm}>Back</button>
    {/if}
</div>

<style lang="scss">
  input[type=file]::file-selector-button {
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
</style>