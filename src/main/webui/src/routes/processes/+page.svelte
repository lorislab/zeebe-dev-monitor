<script lang="ts">
    import {
        Button,
        A,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        TableHead,
        TableHeadCell,
        Table
    } from 'flowbite-svelte';
    import { CloudArrowUp } from 'svelte-heros-v2';
    import DeployProcessModal from "../../components/processes/DeployProcessModal.svelte";
    import TableSearchBar from "../../components/TableSearchBar.svelte";
    import TablePagerBar from "../../components/TablePagerBar.svelte";
    import { createSearchTableStore } from "../../lib/stores/search";
    import {page} from "$app/stores";

    import {invalidate} from "$app/navigation";

    type Process = {
        key: number;
        bpmnProcessId: string;
        version: number;
        countRunning: number;
        countEnded: string;
        timestamp: string;
        searchTerms: string;
    }

    const searchTableStore = createSearchTableStore<Process>(page,
        $p => $p.data.items.map((item: Process) => ({
            ...item,
            searchTerms: `${item.key} ${item.bpmnProcessId}`
        })
    ), 10);

    let deploymentModal = false;

</script>

<TableSearchBar searchStore={searchTableStore} >
    <Button size="sm" on:click={() => deploymentModal = true} class="shadow-md">
        <CloudArrowUp class="mr-2 -ml-1 w-5 h-5 focus:outline-none" variation="solid" />Deploy process</Button>
    <DeployProcessModal bind:open={deploymentModal} />
</TableSearchBar>

<Table hoverable={true} divClass='relative overflow-x-auto sm:rounded-lg' >
    <TableHead>
        <TableHeadCell>Process Definition Key</TableHeadCell>
        <TableHeadCell>BPMN process id</TableHeadCell>
        <TableHeadCell>Version</TableHeadCell>
        <TableHeadCell>#Active</TableHeadCell>
        <TableHeadCell>#Ended</TableHeadCell>
        <TableHeadCell>Deployment Time</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $searchTableStore.paged  as item}
            <TableBodyRow>
                <TableBodyCell><A href="/processes/{item.key}" class="font-medium hover:underline">{item.key}</A></TableBodyCell>
                <TableBodyCell>{item.bpmnProcessId}</TableBodyCell>
                <TableBodyCell>{item.version}</TableBodyCell>
                <TableBodyCell>{item.countRunning}</TableBodyCell>
                <TableBodyCell>{item.countEnded}</TableBodyCell>
                <TableBodyCell>{item.timestamp}</TableBodyCell>
            </TableBodyRow>
        {/each}
    </TableBody>
</Table>
<TablePagerBar searchStore={searchTableStore} />