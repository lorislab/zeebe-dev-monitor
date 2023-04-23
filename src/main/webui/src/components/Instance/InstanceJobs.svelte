<script lang="ts">
    import {
        Button,
        TableHead,
        TableHeadCell,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        ButtonGroup, Table, Badge, Indicator
    } from 'flowbite-svelte';

    import {colorJobStatus} from "$lib/app.js";
    import {Bolt, CursorArrowRays, NoSymbol, Play, PlusCircle} from "svelte-heros-v2";
    import {createSearchTableStore} from "../../lib/stores/search";
    import {page} from "$app/stores";
    import TableSearchBar from "$components/TableSearchBar.svelte";
    import TablePagerBar from "$components/TablePagerBar.svelte";
    import CompleteJobModal from "$components/Instance/CompleteJobModal.svelte";
    import FailJobModal from "$components/Instance/FailJobModal.svelte";
    import ThrowErrorJobModal from "$components/Instance/ThrowErrorJobModal.svelte";
	import type { Job } from '../../models/Job.model';
	import RetriesJobModal from './RetriesJobModal.svelte';
	import { init } from 'svelte/internal';

    const searchTableStore = createSearchTableStore<Job>(page,
        $p => $p.data.instance.jobs.map((item: Job) => ({
                ...item,
                searchTerms: `${item.key} ${item.jobType} ${item.state}`
            })
        ));

    export let elementMouseOver: any;
    export let elementMouseOut: any;

    let completeModal;
    let failModal;
    let throwErrorModal;
    let retriesModal: RetriesJobModal;
</script>

<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Element Id</TableHeadCell>
        <TableHeadCell>Job Key</TableHeadCell>
        <TableHeadCell>Job Type</TableHeadCell>
        <TableHeadCell>Retries</TableHeadCell>
        <TableHeadCell>Error Code</TableHeadCell>
        <TableHeadCell>Error Message</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Time</TableHeadCell>
        <TableHeadCell>Actions</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.elementId)} on:mouseout={elementMouseOut(item.elementId)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.elementId}</TableBodyCell>
                <TableBodyCell>{item.key}</TableBodyCell>
                <TableBodyCell>{item.jobType}</TableBodyCell>
                <TableBodyCell>{item.retries}</TableBodyCell>
                <TableBodyCell>{item.errorCode}</TableBodyCell>
                <TableBodyCell>{item.errorMessage}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorJobStatus[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorJobStatus[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
                <TableBodyCell>
                    <ButtonGroup>
                        <Button on:click={completeModal.init(item)} title="Complete job" disabled='{!item.isActivatable || !$page.data.instance.detail.isRunning}'><Play class="w-4 h-4 focus:outline-none inline-flex"/></Button>
                        <Button on:click={failModal.init(item)} title="Fail job" disabled='{!item.isActivatable || !$page.data.instance.detail.isRunning}'><NoSymbol class="w-4 h-4 focus:outline-none inline-flex"/></Button>
                        <Button on:click={throwErrorModal.init(item)} title="Throw error" disabled='{!item.isActivatable || !$page.data.instance.detail.isRunning}'><Bolt class="w-4 h-4 focus:outline-none inline-flex"/></Button>
                        <Button on:click={() => retriesModal.init(item)} title="Update retries" disabled='{!$page.data.instance.detail.isRunning}'><PlusCircle class="w-4 h-4 focus:outline-none inline-flex"/></Button>
                    </ButtonGroup>
                </TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />

<CompleteJobModal bind:this={completeModal} />
<FailJobModal bind:this={failModal} />
<ThrowErrorJobModal bind:this={throwErrorModal} />
<RetriesJobModal bind:this={retriesModal} />