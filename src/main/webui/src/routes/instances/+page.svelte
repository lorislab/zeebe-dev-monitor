<script lang="ts">
    import {
        A, Badge, Indicator,
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell,
    } from 'flowbite-svelte';
    import {createSearchTableStore} from "../../lib/stores/search";
    import TableSearchBar from "../../components/TableSearchBar.svelte";
    import TablePagerBar from "../../components/TablePagerBar.svelte";
    import {page} from "$app/stores";
    import {colorProcessInstanceState} from "$lib/app.js";

    type Instance = {
        key: number;
        bpmnProcessId: string;
        processDefinitionKey: number;
        state: string;
        start: string;
        end: string;
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<Instance>(page,
        $p => $p.data.items.map((item: Instance) => ({
                ...item,
                searchTerms: `${item.key} ${item.bpmnProcessId} ${item.processDefinitionKey} ${item.state}`
            })
        ), 10 );

</script>

<TableSearchBar searchStore={searchTableStore} />

<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Process Instance Key</TableHeadCell>
        <TableHeadCell>Process Id</TableHeadCell>
        <TableHeadCell>Process Key</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Start Time</TableHeadCell>
        <TableHeadCell>End Time</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><A href="/instances/{item.key}" class="font-medium hover:underline">{item.key}</A></TableBodyCell>
                <TableBodyCell>{item.bpmnProcessId}</TableBodyCell>
                <TableBodyCell><A href="/processes/{item.processDefinitionKey}" class="font-medium hover:underline">{item.processDefinitionKey}</A></TableBodyCell>
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