<script lang="ts">
    import {
        Badge,
        Indicator,
        Label,
        Modal,
        Table,
        TableBody,
        TableBodyCell,
        TableBodyRow,
        Textarea
    } from "flowbite-svelte";
    import type Incident from "./Incidents.svelte";
    import {colorIncidentState} from "$lib/app.js";

    let incident: Incident;
    let open = false;

    export function init(_incident: Incident) {
        incident = _incident;
        open = true;
    }

</script>
<Modal title="Incident details" bind:open={open} class="w-full" autoclose >
    <Table divClass='relative overflow-x-auto border rounded-lg'>
        <TableBody>
            <TableBodyRow>
                <TableBodyCell>Element Id</TableBodyCell>
                <TableBodyCell>{incident.elementId}</TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Incident Key</TableBodyCell>
                <TableBodyCell>{incident.key}</TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Job Key</TableBodyCell>
                <TableBodyCell>{incident.jobKey === -1 ? '' : incident.jobKey}</TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Type</TableBodyCell>
                <TableBodyCell>{incident.errorType}</TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>State</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorIncidentState[incident.state]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorIncidentState[incident.state]}" size="xs" class="mr-1"/>{incident.state}
                    </Badge>
                </TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Created</TableBodyCell>
                <TableBodyCell>{incident.created}</TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Resolved</TableBodyCell>
                <TableBodyCell>{incident.resolved ?? ''}</TableBodyCell>
            </TableBodyRow>
        </TableBody>
    </Table>
    <Label class="space-y-2">
        <span>Error message</span>
        <Textarea rows="6" value={incident.errorMessage}/>
    </Label>
</Modal>