<svelte:head>
    <title>About</title>
    <meta name="description" content="About this app"/>
</svelte:head>

<script lang="ts">
    import {page} from '$app/stores';
</script>

<div class="text-column">
    Your table ID is {$page.params.tableId}
</div>
