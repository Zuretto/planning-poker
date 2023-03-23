
<svelte:head>
	<title>Home</title>
	<meta name="description" content="Svelte demo app" />
</svelte:head>


<script lang="ts">
	import { goto } from '$app/navigation';
	const goToCreateBoard = (): void => {
		goto('add-table');
	}
</script>

<section>
	<h1>
		This is starting page
	</h1>
	<button on:click={goToCreateBoard}>Create board!</button>
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
</style>
