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
    import { createSearchTableStore } from "../../lib/stores/search";
    import TableSearchBar from "../../components/TableSearchBar.svelte";
    import TablePagerBar from "../../components/TablePagerBar.svelte";
    import {page} from "$app/stores";
    import {colorIncidentState} from "$lib/app.js";

    type Incident = {
        key: number;
        processInstanceKey: number;
        bpmnProcessId: string;
        processDefinitionKey: number;
        errorType: string;
        state: string;
        created: string;
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<Incident>(page,
        $p => $p.data.items.map((item: Incident) => ({
                ...item,
                searchTerms: `${item.key} ${item.bpmnProcessId} ${item.processDefinitionKey} ${item.processInstanceKey} ${item.state}`
            })
        ), 10);
</script>

<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto sm:rounded-lg'>
    <TableHead>
        <TableHeadCell>Incident Key</TableHeadCell>
        <TableHeadCell>Instance Key</TableHeadCell>
        <TableHeadCell>Process Id</TableHeadCell>
        <TableHeadCell>Definition Key</TableHeadCell>
        <TableHeadCell>Type</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Created</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell>{item.key}</TableBodyCell>
                <TableBodyCell><A href="/instances/{item.processInstanceKey}" class="font-medium hover:underline">{item.processInstanceKey}</A></TableBodyCell>
                <TableBodyCell>{item.bpmnProcessId}</TableBodyCell>
                <TableBodyCell><A href="/processes/{item.processDefinitionKey}" class="font-medium hover:underline">{item.processDefinitionKey}</A></TableBodyCell>
                <TableBodyCell>{item.errorType}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorIncidentState[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorIncidentState[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.created}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />