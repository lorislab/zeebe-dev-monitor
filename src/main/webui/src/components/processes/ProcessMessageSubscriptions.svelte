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
    import {Envelope, CursorArrowRays} from "svelte-heros-v2";
    import SendMessageModal from "./SendMessageModal.svelte";
    import TableSearchBar from "../TableSearchBar.svelte";
    import TablePagerBar from "../TablePagerBar.svelte";
    import {createSearchTableStore } from "../../lib/stores/search";
    import {page} from "$app/stores";
    import {colorMessageSubscriptionState} from "$lib/app.js";

    type Message = {
        elementId: number;
        messageName: string;
        state: string;
        timestamp: string;
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<Message>(page,
        $p => $p.data.process.messageSubscriptions.map((item: Message) => ({
                ...item,
                searchTerms: `${item.messageName}`
            })
        ));

    let sendMessageModel;
    export let elementMouseOver;
    export let elementMouseOut;
</script>


<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Element Id</TableHeadCell>
        <TableHeadCell>Message Name</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Time</TableHeadCell>
        <TableHeadCell>Actions</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.elementId)} on:mouseout={elementMouseOut(item.elementId)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.elementId}</TableBodyCell>
                <TableBodyCell>{item.messageName}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorMessageSubscriptionState[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorMessageSubscriptionState[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
                <TableBodyCell>
                    <ButtonGroup>
                        <Button on:click={sendMessageModel.init(item.messageName)} ><Envelope class="w-4 h-4 mr-2 focus:outline-none" tooltip="Send message"/></Button>
                    </ButtonGroup>
                </TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />

<SendMessageModal bind:this={sendMessageModel} />
