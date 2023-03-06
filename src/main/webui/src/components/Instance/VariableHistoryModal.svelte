<script lang="ts">
    import {Modal, Table, TableBodyRow, TableBodyCell, TableBody, TableHead, TableHeadCell} from "flowbite-svelte";
    import {createSearchTableStore} from "../../lib/stores/search";
    import {page} from "$app/stores";
    import {writable} from "svelte/store";
    import TablePagerBar from "$components/TablePagerBar.svelte";

    let open = false;

    let name;
    let scope;
    let data;

    export function init(s, n, d) {
        name = n;
        scope = s;
        data = d;
        history.set(d);
        open = true;
    }

    type VariableHistory = {
        timestamp: string
        value: string
    }

    let history = writable<VariableHistory[]>([]);

    const searchTableStore = createSearchTableStore<VariableHistory>(history,
        $p => $p.map((item: VariableHistory) => ({
                ...item,
                searchTerms: ``
            })
        ), 10 );

 </script>

<Modal title="History of '{name}' variable" bind:open={open} class="w-full" >
    <Table divClass='relative overflow-x-auto border rounded-lg'>
        <TableHead>
            <TableHeadCell>Time</TableHeadCell>
            <TableHeadCell>Value</TableHeadCell>
        </TableHead>
        <TableBody>
            {#each $searchTableStore.paged as item}
                <TableBodyRow>
                    <TableBodyCell>{item.timestamp}</TableBodyCell>
                    <TableBodyCell>{item.value}</TableBodyCell>
                </TableBodyRow>
            {/each}
        </TableBody>
    </Table>
    <TablePagerBar searchStore={searchTableStore} />
</Modal>
