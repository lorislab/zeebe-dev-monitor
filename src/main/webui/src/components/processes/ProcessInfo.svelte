<script lang="ts">
    import {Table, TableBody, TableBodyCell, TableBodyRow, TableHead, TableHeadCell} from 'flowbite-svelte';
    export let info;

</script>

<Table divClass="relative overflow-x-auto border rounded-lg">
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
            <TableBodyCell>Deploy time</TableBodyCell>
            <TableBodyCell>{info.timestamp}</TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>#Active</TableBodyCell>
            <TableBodyCell>{info.countRunning}</TableBodyCell>
        </TableBodyRow>
        <TableBodyRow>
            <TableBodyCell>#Ended</TableBodyCell>
            <TableBodyCell>{info.countEnded}</TableBodyCell>
        </TableBodyRow>
    </TableBody>
</Table>
