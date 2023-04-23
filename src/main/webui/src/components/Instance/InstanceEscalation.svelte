<script lang="ts">
    import {
        TableHead,
        TableHeadCell,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        ButtonGroup, Table, Badge, Indicator, MenuButton, Dropdown, DropdownItem
    } from 'flowbite-svelte';
    import TableSearchBar from "$components/TableSearchBar.svelte";
    import TablePagerBar from "$components/TablePagerBar.svelte";
    import {CursorArrowRays} from "svelte-heros-v2";
    import {createSearchTableStore} from "../../lib/stores/search";
    import {page} from "$app/stores";

    type Escalation = {
        key: number
        processInstanceKey: number
        catchElementId: string
        throwElementId: string
        escalationCode: string
        timestamp: string        
        searchTerms: string
    }

    const searchTableStore = createSearchTableStore<Escalation>(page,
        $p => $p.data.instance.escalations.map((item: Escalation) => ({
                ...item,
                searchTerms: `${item.escalationCode} ${item.catchElementId} ${item.throwElementId}`
            })
        ));

    export let elementMouseOver;
    export let elementMouseOut;
</script>

<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Throw Element Id</TableHeadCell>
        <TableHeadCell>Catch Element Id</TableHeadCell>
        <TableHeadCell>Code</TableHeadCell>
        <TableHeadCell>Time</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.throwElementId)} on:mouseout={elementMouseOut(item.throwElementId)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.throwElementId}</TableBodyCell>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.catchElementId)} on:mouseout={elementMouseOut(item.catchElementId)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.catchElementId}</TableBodyCell>                
                <TableBodyCell>{item.escalationCode}</TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />
