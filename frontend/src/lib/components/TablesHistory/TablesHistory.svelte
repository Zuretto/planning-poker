<script lang="ts">
    import { getTables } from "../../util/api-handler";
    import { accountStore } from "../../util/store";
    import type { GameHistoryResponse } from "../../util/api-handler.models";

    const gameHistoryListPromise: Promise<GameHistoryResponse[]> = getTables($accountStore.username);
</script>

<div class="wrapper">
    {#await gameHistoryListPromise}
    {:then gameHistoryList}
        <ul>
            {#each gameHistoryList as gameHistory}
                <li> <a href="/table/{gameHistory.gameId}">GO TO</a>
                    creator: {gameHistory.creator}, players:
                    {#each gameHistory.players as player} {player} {/each}, user stories:
                    <ul>
                        {#each gameHistory.userStories as userStory}
                            <li>
                                name: {userStory.name}
                                estimation: {userStory.estimationAverage ?? 'not yet estimated'}
                            </li>
                        {/each}
                    </ul>
                </li>
            {/each}
        </ul>
    {/await}
</div>

<style>
    .wrapper {
        width: 100%;
        border-radius: 20px;
        border: 1px solid rgba(0, 0, 0, 0.2);
    }
</style>
