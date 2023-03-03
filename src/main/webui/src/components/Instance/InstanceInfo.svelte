<script lang="ts">
    import {
        A,
        Badge,
        Button,
        Indicator,
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow
    } from 'flowbite-svelte';
    import {colorProcessInstanceState} from "$lib/app.js";
    import CancelInstanceModal from "$components/Instance/CancelInstanceModal.svelte";
    import {NoSymbol} from "svelte-heros-v2";
    export let info;
    let cancelModal = false;
</script>

<div class="flex flex-row mb-2">
    <div class="flex-auto"></div>
    <div>
        <Button size="xs" color="{info.isRunning ? 'red' : 'alternative'}" class="shadow-md" on:click={() => cancelModal = true} disabled='{!info.isRunning}'>
            <NoSymbol class="mr-2 -ml-1 w-5 h-5 focus:outline-none" variation="solid" />Cancel
        </Button>
    </div>
</div>

<Table>
    <TableBody>
        <TableBodyRow>
            <TableBodyCell>Key</TableBodyCell>
            <TableBodyCell>{info.key}</TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>BPMN process id</TableBodyCell>
            <TableBodyCell>{info.bpmnProcessId}</TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>Version</TableBodyCell>
            <TableBodyCell>{info.version}</TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>Process Definition Key</TableBodyCell>
            <TableBodyCell><A href="/processes/{info.processDefinitionKey}" class="font-medium hover:underline">{info.processDefinitionKey}</A></TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>State</TableBodyCell>
            <TableBodyCell>
                    <Badge color="{colorProcessInstanceState[info.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorProcessInstanceState[info.state]}" size="xs" class="mr-1"/>{info.state}
                    </Badge>
            </TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>Start time</TableBodyCell>
            <TableBodyCell>{info.start}</TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>End time</TableBodyCell>
            <TableBodyCell>{info.end ?? ''}</TableBodyCell>
        </TableBodyRow>
        {#if info.parentProcessInstanceKey > 0}
        <TableBodyRow>
            <TableBodyCell>Parent Process Instance</TableBodyCell>
            <TableBodyCell><A href="/instances/{info.parentProcessInstanceKey}" class="font-medium hover:underline">{info.parentProcessInstanceKey}</A></TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>Parent Process Id</TableBodyCell>
            <TableBodyCell>{info.parentBpmnProcessId}</TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>Parent Definition Key</TableBodyCell>
            <TableBodyCell><A href="/processes/{info.parentProcessDefinitionKey}" class="font-medium hover:underline">{info.parentProcessDefinitionKey}</A></TableBodyCell>
        </TableBodyRow>
        {/if}
    </TableBody>
</Table>
<CancelInstanceModal bind:open={cancelModal} />