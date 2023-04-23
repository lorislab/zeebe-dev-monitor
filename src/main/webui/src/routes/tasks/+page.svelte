<script lang="ts">
    import {
        A, Badge, Button, Indicator,
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell,
        ButtonGroup
    } from 'flowbite-svelte';
    import {Play, InformationCircle} from "svelte-heros-v2";
    import { createSearchTableStore} from "../../lib/stores/search";
    import TableSearchBar from "../../components/TableSearchBar.svelte";
    import TablePagerBar from "../../components/TablePagerBar.svelte";
    import {page} from "$app/stores";
    import {colorJobStatus} from "$lib/app.js";
	import CompleteUserTaskModal from '$components/Instance/CompleteUserTaskModal.svelte';
	import type { UserTask } from '../../models/UserTask.model';
	import InfoUserTaskModal from '$components/Instance/InfoUserTaskModal.svelte';

    const searchTableStore = createSearchTableStore<UserTask>(page,
        $p => $p.data.items.map((item: UserTask) => ({
                ...item,
                searchTerms: `${item.assignee} ${item.groups} ${item.users} ${item.processInstanceKey} ${item.status}`
            })
        ), 10);

    let completeModal: CompleteUserTaskModal;
    let infoModal: InfoUserTaskModal;
</script>

<TableSearchBar searchStore={searchTableStore} />
<Table data-testid="userTasksTable" hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Instance Key</TableHeadCell>
        <TableHeadCell>Element</TableHeadCell>
        <TableHeadCell>Assignee</TableHeadCell>
        <TableHeadCell>Due Date</TableHeadCell>
        <TableHeadCell>Follow Up</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Created</TableHeadCell>
        <TableHeadCell>Actions</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
        <TableBodyRow>
            <TableBodyCell><A href="/instances/{item.processInstanceKey}" class="font-medium hover:underline">{item.processInstanceKey}</A></TableBodyCell>
            <TableBodyCell>{item.elementId}</TableBodyCell>
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
                    <Button on:click={() => completeModal.init(item)} title="Complete user task" disabled='{!item.isActivatable}'><Play class="w-4 h-4 focus:outline-none inline-flex"/></Button>
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