<script lang="ts">
    import { getTables } from "../../util/api-handler";
    import { accountStore } from "../../util/store";
    import type { GameHistoryResponse } from "../../util/api-handler.models";

    const gameHistoryListPromise: Promise<GameHistoryResponse[]> = getTables($accountStore.username);
</script>

{#await gameHistoryListPromise}
{:then gameHistoryList}
    {#if (gameHistoryList.length)}
        <div class="wrapper">
            <ul class="stories-container">
                {#each gameHistoryList as gameHistory}
                    <li class="container">
                        <div class="container-header">
                            <span>{gameHistory.creator}'s game</span>
                            <a href="/table/{gameHistory.gameId}">GO TO</a>
                        </div>
                        <div>
                            players:
                            {#each gameHistory.players as player}
                                <li class="player">{player} </li>
                            {/each}
                        </div>
                        {#if (!!gameHistory.userStories.length)}
                            user stories:

                            <ul>
                                {#each gameHistory.userStories as userStory}
                                    <li>{userStory.name}, <b>{userStory.estimationAverage ?? 'not estimated'} </b>
                                    </li>
                                {/each}
                            </ul>
                        {:else}
                            No user stories 😭😭😭
                        {/if}
                    </li>
                {/each}
            </ul>
        </div>
    {/if}
{/await}

<style lang="scss">
  .wrapper {
    width: 100%;
    border-radius: 20px;
    border: 1px solid rgba(0, 0, 0, 0.2);
    padding: 1rem;

    .stories-container {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      gap: 1rem;
      text-align: left;

      .container-header {
        width: 100%;
        display: flex;
        justify-content: space-between;
      }

      .player {
        list-style: none;
        display: inline-block;
      }

      .player:first-of-type {
        font-weight: bold;
      }

      .player + .player:before {
        content: ", ";
      }
    }

    .container {
      border-radius: 20px;
      border: 1px solid rgba(0, 0, 0, 0.2);
      padding: 1rem;
      min-width: 20rem;
    }

    ul {
      list-style-type: none;

      li {
        ul {
          list-style-type: circle;
          text-align: left
        }
      }
    }
  }
</style>
