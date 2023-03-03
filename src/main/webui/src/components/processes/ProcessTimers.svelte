<script lang="ts">
    import { Table,  TableHead, TableHeadCell, TableBody, TableBodyCell, TableBodyRow } from 'flowbite-svelte';
    import TablePagerBar from "../TablePagerBar.svelte";
    import TableSearchBar from "../TableSearchBar.svelte";
    import { createSearchTableStore } from "../../lib/stores/search";
    import {page} from "$app/stores";

    type Timer = {
        targetElementId: string;
        dueDate: string;
        repetitions: number;
        state: string;
        timestamp: string;
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<Timer>(page,
        $p => $p.data.process.timers.map((item: Timer) => ({
                ...item,
                searchTerms: `${item.targetElementId} ${item.state}`
            })
        ));

</script>

<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto sm:rounded-lg'>
    <TableHead>
        <TableHeadCell>Element Id</TableHeadCell>
        <TableHeadCell>Due Date</TableHeadCell>
        <TableHeadCell>Repetitions</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Time</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell>{item.targetElementId}</TableBodyCell>
                <TableBodyCell>{item.dueDate}</TableBodyCell>
                <TableBodyCell>{item.repetitions}</TableBodyCell>
                <TableBodyCell>{item.state}</TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />