<script lang="ts">
    import TableSearchBar from "$components/TableSearchBar.svelte";
    import {
        Badge,
        Button, ButtonGroup, Dropdown, DropdownItem, Indicator, MenuButton, P,
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell, Textarea
    } from "flowbite-svelte";
    import {CursorArrowRays, Envelope, Wrench} from "svelte-heros-v2";
    import TablePagerBar from "$components/TablePagerBar.svelte";
    import {createSearchTableStore} from "../../lib/stores/search";
    import {page} from "$app/stores";
    import {colorIncidentState} from "$lib/app.js";
    import ResolveIncidentModal from "$components/Instance/ResolveIncidentModal.svelte";

    type Incident = {
        key : number
        bpmnProcessId: string
        elementId: string
        elementInstanceKey: string
        jobKey: number
        payload: string
        errorType: string
        errorMessage: string
        state: string
        created: string
        resolved: string
        isResolved: boolean
    }

    export let elementMouseOver;
    export let elementMouseOut;

    let resolveModal;

    const searchTableStore = createSearchTableStore<Incident>(page,
        $p => $p.data.instance.incidents.map((item: Incident) => ({
                ...item,
                searchTerms: `${item.key} ${item.errorMessage}`
            })
        ) );
</script>

<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg' >
    <TableHead>
        <TableHeadCell>Element Id</TableHeadCell>
        <TableHeadCell>Incident Key</TableHeadCell>
        <TableHeadCell>Job Key</TableHeadCell>
        <TableHeadCell>Type</TableHeadCell>
        <TableHeadCell>Message</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Created</TableHeadCell>
        <TableHeadCell>Resolved</TableHeadCell>
        <TableHeadCell>Actions</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.elementId)} on:mouseout={elementMouseOut(item.elementId)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.elementId}</TableBodyCell>
                <TableBodyCell>{item.key}</TableBodyCell>
                <TableBodyCell>{item.jobKey === -1 ? '': item.jobKey}</TableBodyCell>
                <TableBodyCell>{item.errorType}</TableBodyCell>
                <TableBodyCell class="whitespace-pre-wrap ">{item.errorMessage}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorIncidentState[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorIncidentState[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.created}</TableBodyCell>
                <TableBodyCell>{item.resolved ?? ''}</TableBodyCell>
                <TableBodyCell>
                    {#if !item.isResolved && $page.data.instance.detail.isRunning}
                        <ButtonGroup>
                            <Button on:click={resolveModal.init(item)} title="Resolve incident"><Wrench class="w-4 h-4 focus:outline-none inline-flex"/></Button>
                        </ButtonGroup>
                    {/if}
                </TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />

<ResolveIncidentModal bind:this={resolveModal} />