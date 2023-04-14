<script>
    export let number
</script>

<button class="card">{number}</button>

<style>
    .card {
        padding: 4rem 0;
        border-radius: 8px;
        border: 1px solid transparent;
        font-size: 2rem;
        font-weight: 500;
        font-family: inherit;
        cursor: pointer;
        transition: transform 0.25s, border-color 0.25s;
        flex: 0 0 100px;
        text-align: center;
    }
    .card:hover,
    .card:focus,
    .card:focus-visible{
        box-shadow: 0 0.5em 0.5em -0.4em #646cff;
        transform: translateY(-0.25em);
        border-color: #646cff;
    }
</style>