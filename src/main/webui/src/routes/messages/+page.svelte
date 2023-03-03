<script lang="ts">
    import {
        Badge,
        Button, Indicator,
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell
    } from 'flowbite-svelte';
    import {Envelope} from "svelte-heros-v2";
    import {createSearchTableStore} from "../../lib/stores/search";
    import TableSearchBar from "../../components/TableSearchBar.svelte";
    import TablePagerBar from "../../components/TablePagerBar.svelte";
    import {page} from "$app/stores";
    import {colorMessageState} from "$lib/app.js";
    import SendMessageModal from "$components/processes/SendMessageModal.svelte";

    type Message = {
        name: string;
        correlationKey: string;
        messageId: string;
        state: string;
        timestamp: string;
        searchTerms: string;
    }


    const searchTableStore = createSearchTableStore<Message>(page,
        $p => $p.data.items.map((item: Message) => ({
                ...item,
                searchTerms: `${item.name} ${item.correlationKey} ${item.messageId} ${item.state}`
            })
        ));
    let sendMessageModel;
</script>

<TableSearchBar searchStore={searchTableStore} >
    <Button size="sm" class="shadow-md" on:click={sendMessageModel.init(null, null, true)}><Envelope class="w-4 h-4 mr-2 focus:outline-none" />Send message</Button>
</TableSearchBar>

<Table hoverable={true} divClass='relative overflow-x-auto sm:rounded-lg'>
    <TableHead>
        <TableHeadCell>Name</TableHeadCell>
        <TableHeadCell>Correlation Key</TableHeadCell>
        <TableHeadCell>Message Id</TableHeadCell>
        <TableHeadCell>State</TableHeadCell>
        <TableHeadCell>Timestamp</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell>{item.name}</TableBodyCell>
                <TableBodyCell>{item.correlationKey}</TableBodyCell>
                <TableBodyCell>{item.messageId}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorMessageState[item.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorMessageState[item.state]}" size="xs" class="mr-1"/>{item.state}
                    </Badge>
                </TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />

<SendMessageModal bind:this={sendMessageModel} />