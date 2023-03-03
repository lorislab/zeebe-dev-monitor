<script lang="ts">
    import {Button, Input} from "flowbite-svelte";
    import {ChevronLeft, ChevronRight, MagnifyingGlass} from "svelte-heros-v2";
    import type {SearchStore} from "../lib/stores/search.ts";

    export let searchStore: SearchStore<any>;

    let activeClass = 'px-4 py-2.5 text-sm text-blue-600 border border-gray-300 bg-blue-50 hover:bg-blue-100 hover:text-blue-700 dark:border-gray-700 dark:bg-gray-700 dark:text-white';
    let normalClass = `border px-4 py-2.5 text-sm font-medium text-gray-900 bg-white border-t border-b border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10`;
</script>

<div class="flex flex-row mb-2">
    <div>
        <Input size="sm" type="search" class="mt-2" bind:value={$searchStore.search} >
            <MagnifyingGlass slot="left" class="w-5 h-5" />
        </Input>
    </div>
    <div class="flex-auto"></div>
    {#if $searchStore.pageCount > 1}
    <div>
        <div class="text-sm text-gray-700 dark:text-gray-400 mr-4 align-text-bottom py-2.5">
            Showing <span class="font-semibold text-gray-900 dark:text-white">{$searchStore.itemBegin}</span> to
            <span class="font-semibold text-gray-900 dark:text-white">{$searchStore.itemEnd}</span>
            of <span class="font-semibold text-gray-900 dark:text-white">{$searchStore.filtered.length}</span> Entries
        </div>
    </div>
    <div class="flex-auto"></div>
    <div>
        <div class="inline-flex rounded-md shadow-sm mr-2 " role="group">
            <Button on:click={searchStore.prevPage}
                    size="xs" btnClass="px-3 py-2.5 text-sm font-medium text-gray-900 bg-white border border-gray-200 rounded-l-lg hover:bg-gray-100 hover:text-blue-700 focus:z-10">
                <ChevronLeft class="w-5 h-5 focus:outline-none"/>
            </Button>
            {#each Array($searchStore.pageCount) as _, index}
                <Button on:click={() => searchStore.setPage(index+1)}
                        btnClass='{$searchStore.currentPage === index+1 ? activeClass : normalClass}' >
                    {index+1}
                </Button>
            {/each}
            <Button on:click={searchStore.nextPage}
                    btnClass="px-3 py-2.5 text-sm font-medium text-gray-900 bg-white border-t border-b rounded-r-lg border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10 ">
                <ChevronRight class="w-5 h-5 focus:outline-none"/>
            </Button>
        </div>
    </div>
    {/if}
</div>