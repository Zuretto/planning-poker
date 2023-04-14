<script lang="ts">
    import {Router, Route} from 'svelte-routing'
    import MainView from './lib/components/MainView/MainView.svelte'
    import CreateTable from "./lib/components/CreateTable/CreateTable.svelte";
    import TableDisplayer from "./lib/components/TableDisplayer/TableDisplayer.svelte";
</script>

<main>

    <Router>
        <Route component={MainView}></Route>
        <Route path="add-table" component={CreateTable}></Route>
        <Route path="table/:tableId" component={TableDisplayer}></Route>
    </Router>

</main>

<style>
    main {
        height: 100%;
    }
</style>
