<script lang="ts">
    import {
        TableHead,
        TableHeadCell,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        Table
    } from 'flowbite-svelte';

    import {createSearchTableStore} from "../../lib/stores/search";
    import {page} from "$app/stores";
    import TableSearchBar from "$components/TableSearchBar.svelte";
    import TablePagerBar from "$components/TablePagerBar.svelte";


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
        $p => $p.data.instance.errors.map((item: Error) => ({
                ...item,
                searchTerms: `${item.processInstanceKey} ${item.exceptionMessage}`
            })
        ));

</script>

<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Position</TableHeadCell>
        <TableHeadCell>Error Event Position</TableHeadCell>
        <TableHeadCell>Exception Message</TableHeadCell>
        <TableHeadCell>Stacktrace</TableHeadCell>
        <TableHeadCell>Created</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell>{item.position}</TableBodyCell>
                <TableBodyCell>{item.errorEventPosition}</TableBodyCell>
                <TableBodyCell class="max-w-[150px] text-ellipsis overflow-hidden" title={item.exceptionMessage}>{item.exceptionMessage}</TableBodyCell>
                <TableBodyCell class="max-w-[150px] text-ellipsis overflow-hidden" title={item.stacktrace}>{item.stacktrace}</TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />
