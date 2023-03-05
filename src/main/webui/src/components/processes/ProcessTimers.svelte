<script lang="ts">
    import {
        Table,
        TableHead,
        TableHeadCell,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        Badge,
        Indicator
    } from 'flowbite-svelte';
    import TablePagerBar from "../TablePagerBar.svelte";
    import TableSearchBar from "../TableSearchBar.svelte";
    import { createSearchTableStore } from "../../lib/stores/search";
    import {page} from "$app/stores";
    import {colorTimerState} from "$lib/app.js";

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
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
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
                <TableBodyCell>
                    <Badge color="{colorTimerState[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorTimerState[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />