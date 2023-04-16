<script lang="ts">
    import type { PlayerResponse } from "../../util/api-handler.models";
    import checkmarkIcon from '../../../assets/check-mark.svg';
    import { Card, cardEnumToDisplayText } from "../../util/enums";

    export let isSelectionVisible;
    export let player: PlayerResponse;
</script>

<div class="read-only-card-wrapper">
    <div class="read-only-card-card">
        {#if !isSelectionVisible && player.selectedCard !== Card.NONE}
            <img class="checkmark-icon"
                 src="{checkmarkIcon}"
                 alt="Checkmark Icon"/>
        {:else if isSelectionVisible}
            <span class="selected-estimation">
                {cardEnumToDisplayText[player.selectedCard]}
            </span>
        {/if}

    </div>
    <span class="read-only-card-caption">
        {player.name}
    </span>
</div>
<!--TODO: card that is grey when -->

<style lang="scss">

  $cardWidth: 5rem;
  $cardHeight: $cardWidth * 1.5;
  $iconSide: $cardWidth * 0.8;
  $iconPaddingTop: $cardWidth * 0.1;

  .read-only-card-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;

    .read-only-card-card {
      background-color: whitesmoke;
      width: $cardWidth;
      height: $cardHeight;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      border-radius: 8px;

      .checkmark-icon {
        width: $iconSide;
        height: $iconSide;
        // filter below is #646cff
        filter: invert(52%) sepia(66%) saturate(5012%) hue-rotate(220deg) brightness(99%) contrast(105%);
      }

      .selected-estimation {
        font-size: 1.5rem;
      }
    }

    .read-only-card-caption {

    }
  }


</style>




