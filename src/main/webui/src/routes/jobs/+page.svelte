<script lang="ts">
    import {
        A, Badge, Indicator,
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell
    } from 'flowbite-svelte';
    import { createSearchTableStore} from "../../lib/stores/search";
    import TableSearchBar from "../../components/TableSearchBar.svelte";
    import TablePagerBar from "../../components/TablePagerBar.svelte";
    import {page} from "$app/stores";
    import {colorJobState} from "$lib/app.js";

    type Job = {
        key: number;
        jobType: string;
        processInstanceKey: number;
        retries: number;
        state: string;
        timestamp: string;
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<Job>(page,
        $p => $p.data.items.map((item: Job) => ({
                ...item,
                searchTerms: `${item.key} ${item.jobType} ${item.processInstanceKey} ${item.state}`
            })
        ), 10);

</script>
<TableSearchBar searchStore={searchTableStore} />
<Table data-testid="jobsTable" hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Job Key</TableHeadCell>
        <TableHeadCell>Job Type</TableHeadCell>
        <TableHeadCell>Process Instance Key</TableHeadCell>
        <TableHeadCell>Retries</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Timestamp</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell>{item.key}</TableBodyCell>
                <TableBodyCell>{item.jobType}</TableBodyCell>
                <TableBodyCell><A href="/instances/{item.processInstanceKey}" class="font-medium hover:underline">{item.processInstanceKey}</A></TableBodyCell>
                <TableBodyCell>{item.retries}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorJobState[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorJobState[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />