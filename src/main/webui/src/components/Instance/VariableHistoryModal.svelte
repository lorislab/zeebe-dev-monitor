<script lang="ts">
    import { Modal, Table, TableBodyRow, TableBodyCell, TableBody} from "flowbite-svelte";
    import {createSearchTableStore} from "../../lib/stores/search";
    import {page} from "$app/stores";

    let open = false;

    let name;
    let scope;
    let data;

    export function init(s, n, d) {
        name = n;
        scope = s;
        data = d;
        open = true;
    }

    type VariableHistory = {
        timestamp: string
        value: string
    }

 </script>

<Modal title="History of '{name}' variable" bind:open={open} size="xs" class="w-full" autoclose>
    <Table>
        <TableBody>
            {#each data as item}
                <TableBodyRow>
                    <TableBodyCell>{item.timestamp}</TableBodyCell>
                    <TableBodyCell>{item.value}</TableBodyCell>
                </TableBodyRow>
            {/each}
        </TableBody>
    </Table>
</Modal>