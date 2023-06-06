<script lang="ts">
    import {Router, Route} from 'svelte-routing'
    import MainView from './lib/components/MainView/MainView.svelte'
    import TableDisplayer from "./lib/components/TableDisplayer/TableDisplayer.svelte";
</script>

<main>

    <Router>
        <Route component={MainView}></Route>
        <Route path="table/:tableId" component={TableDisplayer}></Route>
    </Router>

</main>

<style>
    main {
        height: 100%;
        width: calc(min(calc(90rem), 100vw - 64px));
        margin: auto;
    }
</style>
