<script lang="ts">
    import {
        TableHead,
        TableHeadCell,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        Button, ButtonGroup, Table, MenuButton, Dropdown, DropdownItem, Popover
    } from 'flowbite-svelte';
    import {Cog8Tooth, CursorArrowRays, DocumentPlus, Pencil, Signal} from "svelte-heros-v2";
    import UpdateVariableModal from "./UpdateVariableModal.svelte";
    import VariableHistoryModal from "./VariableHistoryModal.svelte";
    import TableSearchBar from "../TableSearchBar.svelte";
    import TablePagerBar from "../TablePagerBar.svelte";
    import { createSearchTableStore } from "../../lib/stores/search";
    import { page } from '$app/stores'
    import CreateVariableModal from "$components/Instance/CreateVariableModal.svelte";
    import {Tooltip} from "flowbite";

    let updateVariableModel;
    let historyVariableModal;
    let createVariableModal;

    export let elementMouseOver;
    export let elementMouseOut;
    // export let variables;

    type Variable = {
        scopeKey: string;
        elementId: string;
        name: string;
        value: string;
        timestamp: string;
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<Variable>(page,
        $p => $p.data.instance.variables.map((item: Variable) => ({
                ...item,
                searchTerms: `${item.scopeKey} ${item.elementId} ${item.name}`
            })
        ) );


</script>

<UpdateVariableModal bind:this={updateVariableModel} />
<VariableHistoryModal bind:this={historyVariableModal} />
<CreateVariableModal bind:this={createVariableModal} activeScopes={$page.data.instance.activeScopes}  />

<TableSearchBar searchStore={searchTableStore} >
    <Button size="sm" class="shadow-md" color="{$page.data.instance.detail.isRunning ? 'blue' : 'alternative'}" on:click={createVariableModal.init} disabled='{!$page.data.instance.detail.isRunning}' >
        <DocumentPlus class="mr-2 -ml-1 w-5 h-5 focus:outline-none" variation="solid" />Create variable
    </Button>
</TableSearchBar>
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Scope Key</TableHeadCell>
        <TableHeadCell>Element Id</TableHeadCell>
        <TableHeadCell>Name</TableHeadCell>
        <TableHeadCell>Value</TableHeadCell>
        <TableHeadCell>Time</TableHeadCell>
        <TableHeadCell>Actions</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell>{item.scopeKey}</TableBodyCell>
                <TableBodyCell ><CursorArrowRays on:mouseover={elementMouseOver(item.elementId)} on:mouseout={elementMouseOut(item.elementId)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.elementId}</TableBodyCell>
                <TableBodyCell>{item.name}</TableBodyCell>
                <TableBodyCell>{item.value}</TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
                <TableBodyCell >
                    <ButtonGroup>
                        <Button title="Show variable history" on:click={historyVariableModal.init(item.scopeKey, item.name, item.values)}><Signal class="w-4 h-4 focus:outline-none inline-flex" /></Button>
                        <Button title="Edit variable" on:click={updateVariableModel.init(item.scopeKey, item.name, item.value)} disabled='{!$page.data.instance.detail.isRunning}'><Pencil class="w-4 h-4 focus:outline-none inline-flex" /></Button>
                    </ButtonGroup>
                </TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />