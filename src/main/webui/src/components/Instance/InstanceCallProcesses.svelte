<script lang="ts">
    import {
        Table,
        TableHead,
        TableHeadCell,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        A,
        Indicator, Badge
    } from 'flowbite-svelte';
    import TablePagerBar from "../TablePagerBar.svelte";
    import TableSearchBar from "../TableSearchBar.svelte";
    import { createSearchTableStore } from "../../lib/stores/search";
    import {page} from "$app/stores";
    import {CursorArrowRays} from "svelte-heros-v2";
    import {colorCallProcessInstanceState} from "$lib/app.js";

    type Timer = {
        elementId: string
        elementInstanceKey: string
        processDefinitionKey: number
        key: number
        state: string
        bpmnProcessId: string
        searchTerms: string
    }

    export let elementMouseOver;
    export let elementMouseOut;

    const searchTableStore = createSearchTableStore<Timer>(page,
        $p => $p.data.instance.callProcessInstances.map((item: Timer) => ({
                ...item,
                searchTerms: `${item.bpmnProcessId} ${item.state}`
            })
        ));

</script>

<TableSearchBar searchStore={searchTableStore} />pnpm
<Table hoverable={true} divClass='relative overflow-x-auto sm:rounded-lg'>
    <TableHead>
        <TableHeadCell>Element Id</TableHeadCell>
        <TableHeadCell>Instance Key</TableHeadCell>
        <TableHeadCell>Process Id</TableHeadCell>
        <TableHeadCell>Process Definition Key</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.elementId)} on:mouseout={elementMouseOut(item.elementId)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.elementId}</TableBodyCell>
                <TableBodyCell><A href="/instances/{item.key}" class="font-medium hover:underline">{item.key}</A></TableBodyCell>
                <TableBodyCell>{item.bpmnProcessId}</TableBodyCell>
                <TableBodyCell><A href="/processes/{item.processDefinitionKey}" class="font-medium hover:underline">{item.processDefinitionKey}</A></TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorCallProcessInstanceState[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorCallProcessInstanceState[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />