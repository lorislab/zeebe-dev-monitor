<script lang="ts">
    import {
        Button,
        TableHead,
        TableHeadCell,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        ButtonGroup, Table, Badge, Indicator, MenuButton, Dropdown, DropdownItem
    } from 'flowbite-svelte';
    import TableSearchBar from "$components/TableSearchBar.svelte";
    import TablePagerBar from "$components/TablePagerBar.svelte";
    import {colorMessageSubscriptionState} from "$lib/app.js";
    import {CursorArrowRays, Envelope} from "svelte-heros-v2";
    import {createSearchTableStore} from "../../lib/stores/search";
    import {page} from "$app/stores";
    import SendMessageModal from "$components/processes/SendMessageModal.svelte";

    type Message = {
        id: string
        messageName: string
        correlationKey: string
        elementId: string
        elementInstanceKey: number
        processInstanceKey: number
        state: string
        timestamp: string
        open: boolean
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<Message>(page,
        $p => $p.data.instance.messageSubscriptions.map((item: Message) => ({
                ...item,
                searchTerms: `${item.messageName}`
            })
        ));

    let sendMessageModel: any;
    export let elementMouseOver;
    export let elementMouseOut;
</script>

<TableSearchBar searchStore={searchTableStore} />
<Table hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Element Id</TableHeadCell>
        <TableHeadCell>Message Name</TableHeadCell>
        <TableHeadCell>Correlation Key</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Time</TableHeadCell>
        <TableHeadCell>Actions</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.elementId)} on:mouseout={elementMouseOut(item.elementId)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.elementId}</TableBodyCell>
                <TableBodyCell>{item.messageName}</TableBodyCell>
                <TableBodyCell>{item.correlationKey}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorMessageSubscriptionState[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorMessageSubscriptionState[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
                <TableBodyCell>
                    <ButtonGroup>
                        <Button on:click={sendMessageModel.init(item.messageName, item.correlationKey)} title="Send message" disabled='{!$page.data.instance.detail.isRunning}' ><Envelope class="w-4 h-4 focus:outline-none inline-flex"/></Button>
                    </ButtonGroup>
                </TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />

<SendMessageModal bind:this={sendMessageModel} />