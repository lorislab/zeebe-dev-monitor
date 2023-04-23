<script lang="ts">
    import {        
        Button,
        TableHead,
        TableHeadCell,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        Table, Badge, Indicator, ButtonGroup
    } from 'flowbite-svelte';

    import {colorJobStatus} from "$lib/app.js";
    import {CursorArrowRays, InformationCircle, Play} from "svelte-heros-v2";
    import {createSearchTableStore} from "../../lib/stores/search";
    import {page} from "$app/stores";
    import TableSearchBar from "$components/TableSearchBar.svelte";
    import TablePagerBar from "$components/TablePagerBar.svelte";
	import CompleteUserTaskModal from './CompleteUserTaskModal.svelte';
	import type { UserTask } from '../../models/UserTask.model';
	import InfoUserTaskModal from './InfoUserTaskModal.svelte';



    const searchTableStore = createSearchTableStore<UserTask>(page,
        $p => $p.data.instance.userTasks.map((item: UserTask) => ({
                ...item,
                searchTerms: `${item.key} ${item.jobType} ${item.status}`
            })
        ));

    export let elementMouseOver: any;
    export let elementMouseOut: any;

    let completeModal: CompleteUserTaskModal;
    let infoModal: InfoUserTaskModal;
</script>

<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Element Id</TableHeadCell>
        <TableHeadCell>Assignee</TableHeadCell>
        <TableHeadCell>Due Date</TableHeadCell>
        <TableHeadCell>Follow Up Date</TableHeadCell>
        <TableHeadCell>Status</TableHeadCell>
        <TableHeadCell>Time</TableHeadCell>
        <TableHeadCell>Actions</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.elementId)} on:mouseout={elementMouseOut(item.elementId)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.elementId}</TableBodyCell>                
                <TableBodyCell>{item.assignee}</TableBodyCell>
                <TableBodyCell>{item.dueDate}</TableBodyCell>
                <TableBodyCell>{item.followUpDate}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorJobStatus[item.status]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorJobStatus[item.status]}" size="xs" class="mr-1"/>{item.status}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
                <TableBodyCell>
                    <ButtonGroup>
                        <Button on:click={() => completeModal.init(item)} title="Complete job" disabled='{!item.isActivatable || !$page.data.instance.detail.isRunning}'><Play class="w-4 h-4 focus:outline-none inline-flex"/></Button>
                        <Button on:click={() => infoModal.init(item)} title="Details"><InformationCircle class="w-4 h-4 focus:outline-none inline-flex"/></Button>
                    </ButtonGroup>
                </TableBodyCell>                
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />

<CompleteUserTaskModal bind:this={completeModal} />
<InfoUserTaskModal bind:this={infoModal} />