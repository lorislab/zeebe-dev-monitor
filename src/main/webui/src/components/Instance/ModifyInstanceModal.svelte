<script lang="ts">
    import {
        Modal,
        Button,
        TableHead,
        TableBody,
        TableHeadCell,
        Table,
        TableBodyCell,
        TableBodyRow, Listgroup
    } from "flowbite-svelte";
    import {page} from "$app/stores";
    import {Pencil} from "svelte-heros-v2";
    export let open;

    async function modifyProcessInstance() {
        let terminates = [];
        if (te) {
            terminates = te.map(e => e.key);
        }

        let activates = [];
        if (ae) {
            activates = ae.map(e => {
                let vars = {}
                if (e.parameters) {
                    vars = JSON.parse(e.parameters)
                }
                return {id: e.id, ancestor: e.ancestor, vars: vars};
            })
        }


        if (terminates.length === 0 && activates.length === 0) {
            return;
        }

        let data = { terminates: terminates, activates: activates };

        const res = await fetch('/api/instance/' + $page.params.id + '/modify', {
            method: 'PUT', headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        }).then((res) => {
            open = false;
        });
    }

    export type TerminateElement = {
        elementId: string
        key: number
    }

    export type ActiveElement = {
        id: string
        ancestor: number | null
        parameters: string | null
    }

    let te: TerminateElement[] = [];
    let ae: ActiveElement[] = [];

    export const init = (_te: TerminateElement[], _ae: ActiveElement[]) => {
        te = _te;
        ae = _ae;
        open = true;
    }
</script>

<Modal bind:open={open}  autoclose class="w-full">
    <div class="text-center">
        <Pencil class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" size="60"/>
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Are you sure you want to modify this process instance?</h3>
        {#if te.length > 0}
        <div class="mb-4">
            <div class="mb-2">Elements to terminate</div>
            <Listgroup items={te} let:item class="w-full">
                {item.key}-{item.elementId}
            </Listgroup>
        </div>
        {/if}
        {#if ae.length > 0}
        <div class="mb-4">
            <div class="mb-2 ">New active elements</div>
            <Table divClass='relative overflow-x-auto border rounded-lg'>
                <TableHead>
                    <TableHeadCell>Element Id</TableHeadCell>
                    <TableHeadCell>Ancestor Element</TableHeadCell>
                    <TableHeadCell>Parameters</TableHeadCell>
                </TableHead>
                <TableBody>
                    {#each ae as item}
                        <TableBodyRow>
                            <TableBodyCell>{item.id}</TableBodyCell>
                            <TableBodyCell>{item.ancestor ?? ''}</TableBodyCell>
                            <TableBodyCell>{item.parameters ?? ''}</TableBodyCell>
                        </TableBodyRow>
                    {/each}
                </TableBody>
            </Table>
        </div>
        {/if}
        <Button color="red" class="mr-2" on:click={modifyProcessInstance}>Yes, I'm sure</Button>
        <Button color='alternative'>No, cancel</Button>
    </div>
</Modal>