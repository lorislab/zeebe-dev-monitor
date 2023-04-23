<script lang="ts">
    import {
	Badge,
        Button,
        Indicator,
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell
    } from 'flowbite-svelte';
    import {Megaphone} from "svelte-heros-v2";
    import {createSearchTableStore} from "../../lib/stores/search";
    import TableSearchBar from "../../components/TableSearchBar.svelte";
    import TablePagerBar from "../../components/TablePagerBar.svelte";
    import {page} from "$app/stores";
    import {colorSignalStatus} from "$lib/app.js";
    import SendSignalModal from "$components/processes/SendSignalModal.svelte";


    type Signal = {
        key: number;
        name: string;
        status: string;
        variables: string;
        timestamp: string;
        searchTerms: string;
    }


    const searchTableStore = createSearchTableStore<Signal>(page,
        $p => $p.data.items.map((item: Signal) => ({
                ...item,
                searchTerms: `${item.name}`
            })
        ));
    let sendSignalModel: any;
</script>

<TableSearchBar searchStore={searchTableStore} >
    <Button size="sm" class="shadow-md" on:click={sendSignalModel.init(undefined)}><Megaphone class="w-4 h-4 mr-2 focus:outline-none" />Broadcast signal</Button>
</TableSearchBar>

<Table data-testid="signalsTable" hoverable={true} divClass='relative overflow-x-auto border rounded-lg'>
    <TableHead>
        <TableHeadCell>Name</TableHeadCell>
        <TableHeadCell>Variables</TableHeadCell>
        <TableHeadCell>Status</TableHeadCell>
        <TableHeadCell>Timestamp</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged as item}
            <TableBodyRow>
                <TableBodyCell>{item.name}</TableBodyCell>
                <TableBodyCell>{item.variables}</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorSignalStatus[item.status]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorSignalStatus[item.status]}" size="xs" class="mr-1"/>{item.status}
                    </Badge>
                </TableBodyCell>                
                <TableBodyCell>{item.timestamp}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />

<SendSignalModal bind:this={sendSignalModel} />