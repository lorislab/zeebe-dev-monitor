<script lang="ts">
    import {
        A,
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell
    } from 'flowbite-svelte';
    import { createSearchTableStore } from "../../lib/stores/search";
    import TableSearchBar from "../../components/TableSearchBar.svelte";
    import TablePagerBar from "../../components/TablePagerBar.svelte";
    import {page} from "$app/stores";

    type Error = {
        position: number;
        errorEventPosition: number;
        processInstanceKey: number;
        exceptionMessage: string;
        stacktrace: string;
        timestamp: string;
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<Error>(page,
        $p => $p.data.items.map((item: Error) => ({
                ...item,
                searchTerms: `${item.exceptionMessage}`
            })
        ), 10);
</script>

<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Position</TableHeadCell>
        <TableHeadCell>Error Event Position</TableHeadCell>
        <TableHeadCell>Instance Key</TableHeadCell>
        <TableHeadCell>Exception Message</TableHeadCell>
        <TableHeadCell>Stacktrace</TableHeadCell>
        <TableHeadCell>Created</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell>{item.position}</TableBodyCell>
                <TableBodyCell>{item.errorEventPosition}</TableBodyCell>
                <TableBodyCell><A href="/instances/{item.processInstanceKey}" class="font-medium hover:underline">{item.processInstanceKey}</A></TableBodyCell>
                <TableBodyCell class="max-w-[150px] text-ellipsis overflow-hidden" title={item.exceptionMessage}>{item.exceptionMessage}</TableBodyCell>
                <TableBodyCell class="max-w-[150px] text-ellipsis overflow-hidden" title={item.stacktrace}>{item.stacktrace}</TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />