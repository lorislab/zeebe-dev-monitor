<script lang="ts">
    import {
        TableHead,
        TableHeadCell,
        TableBody,
        TableBodyCell,
        TableBodyRow, Table } from 'flowbite-svelte';
    import {CursorArrowRays} from "svelte-heros-v2";
    import { createSearchTableStore } from "../../lib/stores/search";
    import TableSearchBar from "../TableSearchBar.svelte";
    import TablePagerBar from "../TablePagerBar.svelte";
    import {page} from "$app/stores";

    type AuditLog = {
        elementId: string;
        key: string;
        flowScopeKey: string;
        state: string;
        elementName: string;
        bpmnElementType: string;
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<AuditLog>(page,
        $p => $p.data.instance.auditLogEntries.map((item: AuditLog) => ({
                ...item,
                searchTerms: `${item.elementName} ${item.elementId} ${item.elementName}`
            })
        ) );


    export let elementMouseOver;
    export let elementMouseOut;

</script>

<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto rounded-lg border' >
    <TableHead>
        <TableHeadCell>Element Id</TableHeadCell>
        <TableHeadCell>Element Key</TableHeadCell>
        <TableHeadCell>Flow Scope Key</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Name</TableHeadCell>
        <TableHeadCell>Type</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.elementId)} on:mouseout={elementMouseOut(item.elementId)} class="mr-2 -ml-1 w-5 h-5 focus:outline-none inline-flex"/>{item.elementId}</TableBodyCell>
                <TableBodyCell>{item.key}</TableBodyCell>
                <TableBodyCell>{item.flowScopeKey}</TableBodyCell>
                <TableBodyCell>{item.state}</TableBodyCell>
                <TableBodyCell>{item.elementName}</TableBodyCell>
                <TableBodyCell>{item.bpmnElementType}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />