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
    import {Megaphone, CursorArrowRays} from "svelte-heros-v2";
    import TableSearchBar from "../TableSearchBar.svelte";
    import TablePagerBar from "../TablePagerBar.svelte";
    import {createSearchTableStore } from "../../lib/stores/search";
    import {page} from "$app/stores";
    import {colorSignalSubscriptionStatus} from "$lib/app.js";
	import SendSignalModal from './SendSignalModal.svelte';

    type SignalSubscription = {                                            
        key: number;
        name: string;
        status: string;
        timestamp: string;
        bpmnProcessId: string,
        processDefinitionKey: string,
        catchEventId: string,
        catchEventInstanceKey: number,
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<SignalSubscription>(page,
        $p => $p.data.process.signalSubscriptions.map((item: SignalSubscription) => ({
                ...item,
                searchTerms: `${item.name} ${item.status} ${item.processDefinitionKey} ${item.catchEventId} ${item.catchEventInstanceKey}`
            })
        ));

    let sendSignalModel;
    export let elementMouseOver;
    export let elementMouseOut;
</script>


<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Catch Event Id</TableHeadCell>
        <TableHeadCell>Catch Event Instance Key</TableHeadCell>
        <TableHeadCell>Signal Name</TableHeadCell>
        <TableHeadCell>Status</TableHeadCell>
        <TableHeadCell>Time</TableHeadCell>
        <TableHeadCell>Actions</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.catchEventId)} on:mouseout={elementMouseOut(item.catchEventId)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.catchEventId}</TableBodyCell>
                <TableBodyCell>{item.catchEventInstanceKey}</TableBodyCell>
                <TableBodyCell>{item.name}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorSignalSubscriptionStatus[item.status]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorSignalSubscriptionStatus[item.status]}" size="xs" class="mr-1"/>{item.status}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
                <TableBodyCell>
                    <ButtonGroup>
                        <Button on:click={sendSignalModel.init(item.name, false)} ><Megaphone class="w-4 h-4 mr-2 focus:outline-none" tooltip="Broadcast signal"/></Button>
                    </ButtonGroup>
                </TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />

<SendSignalModal bind:this={sendSignalModel} />
