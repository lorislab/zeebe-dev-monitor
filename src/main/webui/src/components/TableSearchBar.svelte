<script lang="ts">
    import {Input} from "flowbite-svelte";
    import {MagnifyingGlass} from "svelte-heros-v2";
    import type { SearchTableInputStore} from "../lib/stores/search.ts";

    export let searchStore: SearchTableInputStore;
    $: se = searchStore.search;
</script>

<div class="flex flex-row mb-2">
    <div>
        <Input size="sm" type="search" bind:value={$se} >
            <MagnifyingGlass slot="left" class="w-5 h-5" />
        </Input>
    </div>
    <div class="flex-auto"></div>
    <div>
        <slot/>
    </div>
</div>