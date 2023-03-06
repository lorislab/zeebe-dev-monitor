<script lang="ts">
    import TableSearchBar from "$components/TableSearchBar.svelte";
    import {
        Badge,
        Button, ButtonGroup, Indicator,
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell,
    } from "flowbite-svelte";
    import {CursorArrowRays, InformationCircle, Wrench} from "svelte-heros-v2";
    import TablePagerBar from "$components/TablePagerBar.svelte";
    import {createSearchTableStore} from "../../lib/stores/search";
    import {page} from "$app/stores";
    import {colorIncidentState} from "$lib/app.js";
    import ResolveIncidentModal from "$components/Instance/ResolveIncidentModal.svelte";
    import InfoIncidentModal from "$components/Instance/InfoIncidentModal.svelte";

    export type Incident = {
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
    let infoModal;

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
                <TableBodyCell>{item.errorType}</TableBodyCell>
                <TableBodyCell class="max-w-[150px] text-ellipsis overflow-hidden" title={item.errorMessage}>{item.errorMessage}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorIncidentState[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorIncidentState[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.created}</TableBodyCell>
                <TableBodyCell>{item.resolved ?? ''}</TableBodyCell>
                <TableBodyCell>
                    <ButtonGroup>
                        <Button on:click={infoModal.init(item)} title="Show details"><InformationCircle class="w-4 h-4 focus:outline-none inline-flex"/></Button>
                        <Button on:click={resolveModal.init(item)} title="Resolve incident" disabled='{item.isResolved || !$page.data.instance.detail.isRunning}'><Wrench class="w-4 h-4 focus:outline-none inline-flex"/></Button>
                    </ButtonGroup>
                </TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />

<ResolveIncidentModal bind:this={resolveModal} />
<InfoIncidentModal bind:this={infoModal} />