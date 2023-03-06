<script lang="ts">
    import {
        A,
        TableHead,
        TableHeadCell,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        Table, Badge, Indicator, Button
    } from 'flowbite-svelte';
    import { createSearchTableStore } from "../../lib/stores/search";
    import TableSearchBar from "../TableSearchBar.svelte";
    import TablePagerBar from "../TablePagerBar.svelte";
    import {page} from "$app/stores";
    import {colorProcessInstanceState} from "$lib/app.js";
    import {Play} from "svelte-heros-v2";
    import CreateNewInstanceModal from "$components/processes/CreateNewInstanceModal.svelte";


    type Instance = {
        key: number;
        state: number;
        start: number;
        end: string;
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<Instance>(page,
        $p => $p.data.process.instances.map((item: Instance) => ({
                ...item,
                searchTerms: `${item.key} ${item.state}`
            })
        ));

    let createInstanceModal = false;

</script>

<TableSearchBar searchStore={searchTableStore} >
    <Button size="sm" on:click={() => createInstanceModal = true} class="shadow-md">
        <Play class="mr-2 -ml-1 w-5 h-5 focus:outline-none" variation="solid" />Create instance</Button>
    <CreateNewInstanceModal  bind:open={createInstanceModal} />
</TableSearchBar>
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Process Instance Key</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Start</TableHeadCell>
        <TableHeadCell>End</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><A href="/instances/{item.key}" class="font-medium hover:underline">{item.key}</A></TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorProcessInstanceState[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorProcessInstanceState[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.start}</TableBodyCell>
                <TableBodyCell>{item.end ?? ''}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />