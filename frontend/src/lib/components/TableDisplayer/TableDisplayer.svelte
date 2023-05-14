<script lang="ts">
    import { usernameStore } from "../../util/store";
    import { joinTable, selectCard, setUserStories } from "../../util/api-handler";
    import { Card } from "../../util/enums.js";
    import TableView from "../TableView/TableView.svelte";
    import SelectCard from "./SelectCard.svelte";
    import Toast from "../Toast/Toast.svelte";
    import type { UserStoryResponse } from "../../util/api-handler.models";

    const baseUrl = import.meta.env.VITE_BASE_URL;


    export let tableId: string;

    const exportLink = `${baseUrl}/poker_api/v1/table/${tableId}/user_stories`

    const cards: Card[] = (Object.values(Card) as Card[])
        .filter(card => card !== Card.NONE);


    let toast: (message) => void;
    let usernameInput: string;

    let selectedCard: Card = Card.NONE;
    let submitted: boolean = false;
    let disabled: boolean = true;

    const joinBoard = (): void => {
        if (!usernameInput || !usernameInput.trim().length) {
            return;
        }
        joinTable(usernameInput, tableId)
            .catch(errorMessage => toast(errorMessage));
    };

    const submitCard = (): void => {
        selectCard($usernameStore, tableId, selectedCard).then(() => {
            disabled = true;
            submitted = true;
        }).catch(errorMessage => toast(errorMessage));
    };

    const onClickCard = (card: Card): void => {
        if (!submitted) {
            selectedCard = card;
            disabled = false;
        }
    };

    const handleReset = (): void => {
        selectedCard = Card.NONE;
        submitted = false;
        disabled = true;
    };

    let round = 0;
    let userStories = []

    const handleRound = (newRound: number): void => {
        round = newRound;
        if (round < userStories.length) currentUserStory = userStories[round];
    }
    const handleUserStories = (newUserStories: UserStoryResponse[]): void => {
        userStories = newUserStories;
        if (round < userStories.length) currentUserStory = userStories[round];
    }

    let currentUserStory: UserStoryResponse = {
        id: null,
        name: "",
        tasks: [],
        estimationAverage: null
    }

    function removeTask(taskIndex) {
        currentUserStory.tasks = currentUserStory.tasks.slice(0, taskIndex).concat(currentUserStory.tasks.slice(taskIndex + 1));
        uploadUserStories()
    }

    function addTask(event) {
        if (event.key === "Enter" && event.target.textContent !== "") {
            const newTask = {
                id: null,
                description: event.target.textContent
            };
            currentUserStory.tasks = [...currentUserStory.tasks, newTask];
            event.target.textContent = "";
        }
        uploadUserStories()
    }

    function uploadUserStories() {
        userStories[round] = currentUserStory;
        setUserStories(tableId, userStories).catch(errorMessage => toast(errorMessage));
    }

    function updateTitle(event) {
        currentUserStory.name = event.target.textContent;
        uploadUserStories();
    }

    function updateTask(taskIndex, event) {
        currentUserStory.tasks[taskIndex].description = event.target.textContent;
        uploadUserStories();
    }

    function preventNewLines(event) {
        if (event.key === "Enter") {
            event.preventDefault();
        }
    }

</script>

<Toast bind:toast="{toast}"/>
{#if $usernameStore === null}
    <div class="text-column">
        <h1> Welcome to the board! </h1>
        <h3> Please enter your name below to proceed: </h3>
        <form on:submit|preventDefault={joinBoard}>
            <input name="username" type="text" id="nickname-input" placeholder="Enter your nickname"
                   bind:value={usernameInput}><br>
            <input type="submit" value="Enter board">
        </form>
    </div>
{:else}
    <div class="wrapper">
        <div class="table-and-user-story">
            <div class="cards-table">
                <TableView username="{$usernameStore}"
                           tableId="{tableId}"
                           resetNotifier="{handleReset}"
                           roundNotifier="{handleRound}"
                           userStoriesNotifier="{handleUserStories}"/>
            </div>
            <div class="user-story-view">
                <h2 contenteditable="true" class:emptyTitle={currentUserStory.name.length === 0}
                    on:blur={() => updateTitle(event)}>{@html currentUserStory.name}</h2>
                <ul>
                    {#each currentUserStory.tasks as task, taskIndex}
                        <div class="task-item">
                            <li contenteditable="true" on:blur={() => {updateTask(taskIndex, event);}}
                                on:keydown={preventNewLines}>{task.description}</li>
                            <button on:click={() => removeTask(taskIndex)} class="inline-button">
                                <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor"
                                     stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                                     class="feather feather-x">
                                    <line x1="18" y1="6" x2="6" y2="18"></line>
                                    <line x1="6" y1="6" x2="18" y2="18"></line>
                                </svg>
                            </button>
                        </div>
                    {/each}
                    <li contenteditable="true" on:keydown={(event) => addTask(event)} on:keydown={preventNewLines}></li>
                </ul>
                <a href={exportLink}><button class="standard-button">Export to Jira</button></a>
            </div>
        </div>
        <button class="submit" on:click={submitCard} {disabled}>Submit</button>
        <div class="cards">
            {#each cards as card}
                <SelectCard on:click={() => onClickCard(card)} card={card} bind:selectedCard={selectedCard}/>
            {/each}
        </div>
    </div>
{/if}

<style>
    .wrapper {
        display: flex;
        flex-direction: column;
        height: 100%;
        width: fit-content;
        margin: auto;
        align-items: stretch;
    }

    .table-and-user-story {
        flex: 1 0 auto;
        display: flex;
        flex-direction: row;
    }

    .cards-table {
        flex: 1 1 auto;
        width: calc(min(calc(90rem), 100vw - 64px) / 3 * 1);
        border-radius: 20px;
        border: 1px solid transparent;
        outline: 2px solid #646cff;
    }

    .user-story-view {
        flex: 5 1 auto;
        width: calc(min(calc(90rem), 100vw - 64px) / 3 * 2);
        margin-left: 1rem;
        margin-right: 1rem;
    }


    .cards {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        flex-shrink: 0;
        gap: 1rem;
        align-self: stretch;
        width: min(calc(90rem), 100vw - 64px);
    }

    .submit {
        align-self: center;
        margin-bottom: 2rem;
        border-radius: 20px;
        border: 1px solid transparent;
        padding: 0.6em 1.2em;
        font-size: 1em;
        font-weight: 500;
        font-family: inherit;
        color: white;
        background-color: #646cff;
        cursor: pointer;
        transition: border-color 0.25s, background-color 0.25s;
        width: 150px;
    }

    .submit:focus,
    .submit:focus-visible {
        outline: 4px auto -webkit-focus-ring-color;
    }

    .submit:disabled {
        background-color: #cccccc
    }

    .emptyTitle {
        border: 1px solid #646cff;
        border-radius: 20px;
    }

    .inline-button {
        display: inline-block;
        border: none;
        background: none;
        padding: 0;
        margin: 0;
        cursor: pointer;
    }

    .task-item {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
    }

    .feather {
        vertical-align: middle;
    }
</style>
