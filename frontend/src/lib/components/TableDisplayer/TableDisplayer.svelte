<script lang="ts">
    // import {usernameStore} from "../../util/store";
    import {accountStore} from "../../util/store";
    import {joinTable, selectCard, setUserStories} from "../../util/api-handler";
    import {Card} from "../../util/enums.js";
    import TableView from "../TableView/TableView.svelte";
    import SelectCard from "./SelectCard.svelte";
    import Toast from "../Toast/Toast.svelte";
    import type {UserStoryResponse} from "../../util/api-handler.models";
    import LoginRegister from "../CreateTable/LoginRegister.svelte";

    const baseUrl = import.meta.env.VITE_BASE_URL;


    export let tableId: string;

    const exportLink = `${baseUrl}/poker_api/v1/table/${tableId}/user_stories`

    const cards: Card[] = (Object.values(Card) as Card[])
        .filter(card => card !== Card.NONE);


    let toast: (message) => void;

    let selectedCard: Card = Card.NONE;
    let submitted: boolean = false;
    let disabled: boolean = true;
    let isInTable : boolean = false;

    const submitCard = (): void => {
        selectCard($accountStore.username, tableId, selectedCard).then(() => {
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
    let userStories = [];

    const handleRound = (newRound: number): void => {
        round = newRound;
        if (round < userStories.length) currentUserStory = userStories[round];
    };

    const handleUserStories = (newUserStories: UserStoryResponse[]): void => {
        userStories = newUserStories;
        if (round < userStories.length) currentUserStory = userStories[round];
    };

    let currentUserStory: UserStoryResponse = {
        id: null,
        name: "",
        tasks: [],
        estimationAverage: null,
    };

    const removeTask = (taskIndex) => {
        if (!confirm('Are you sure you want to drop Task?')) {
            return;
        }
        currentUserStory.tasks = currentUserStory.tasks.slice(0, taskIndex).concat(currentUserStory.tasks.slice(taskIndex + 1));
        uploadUserStories()
    };

    const addTask = (event) => {
        if (event.key === "Enter" && event.target.textContent !== "") {
            const newTask = {
                id: null,
                description: event.target.textContent
            };
            currentUserStory.tasks = [...currentUserStory.tasks, newTask];
            event.target.textContent = "";
        }
        uploadUserStories()
    };

    const uploadUserStories = () => {
        userStories[round] = currentUserStory;
        setUserStories(tableId, userStories).catch(errorMessage => toast(errorMessage));
    };

    const removeUserStory = () => {
        if (!confirm('Are you sure you want to drop User Story?')) {
            return;
        }
        if (round < userStories.length - 1) {
            userStories = userStories.slice(0, round).concat(userStories.slice(round + 1));
            currentUserStory = userStories[round];
        } else {
            currentUserStory = {
                id: null,
                name: "",
                tasks: [],
                estimationAverage: null,
            };
        }
        handleReset();
        uploadUserStories();
    };

    const updateTitle = (event) => {
        currentUserStory.name = event.target.textContent;
        uploadUserStories();
    };

    const updateTask = (taskIndex, event) => {
        currentUserStory.tasks[taskIndex].description = event.target.textContent;
        uploadUserStories();
    };

    const preventNewLines = (event) => {
        if (event.key === "Enter") {
            event.preventDefault();
        }
    };

    accountStore.subscribe(account => {
        if (account == null) return;

        joinTable(account.username, tableId)
            .then(() => isInTable = true)
            .catch(errorMessage => toast(errorMessage))
    })

</script>

<Toast bind:toast="{toast}"/>
{#if !isInTable}
   <LoginRegister/>
{:else}
    <div class="wrapper">
        <div class="table-and-user-story">
            <div class="cards-table">
                <TableView username="{$accountStore.username}"
                           tableId="{tableId}"
                           resetNotifier="{handleReset}"
                           roundNotifier="{handleRound}"
                           userStoriesNotifier="{handleUserStories}"/>
            </div>
            <div class="user-story-view">
                <div class="user-story">
                    <h2 contenteditable="true" class:emptyTitle={currentUserStory.name.length === 0}
                        on:blur={(event) => updateTitle(event)}>{@html currentUserStory.name}</h2>
                    <button on:click="{removeUserStory}" class="inline-button">
                        <svg viewBox="0 0 24 24" width="32" height="32" fill="none" stroke="currentColor"
                             stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                             class="feather feather-x">
                            <line x1="18" y1="6" x2="6" y2="18"></line>
                            <line x1="6" y1="6" x2="18" y2="18"></line>
                        </svg>
                    </button>
                </div>
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
                <a href={exportLink}>
                    <button class="standard-button">Export to Jira</button>
                </a>
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
        width: 33%;
        border-radius: 20px;
        border: 1px solid transparent;
        outline: 2px solid #646cff;
    }

    .user-story-view {
        flex: 5 1 auto;
        width: 66%;
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

    .user-story {
        display: flex;
        flex-direction: row;
        align-items: stretch;
        justify-content: space-between;
        margin-left: 1rem;
        margin-right: 1rem;
    }

    .user-story > h2 {
        flex: 1;
    }

    .task-item {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }

    .task-item > li {
        flex: 1;
        text-align: start;
    }

    .feather {
        vertical-align: middle;
    }
</style>
