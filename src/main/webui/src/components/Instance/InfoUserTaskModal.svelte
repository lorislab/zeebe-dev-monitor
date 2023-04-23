<script lang="ts">
    import {Modal, Button, Textarea, Label, Input, TableHead, TableHeadCell, TableBody, TableBodyRow, TableBodyCell, Table, A, Badge, Indicator} from "flowbite-svelte";
    import {Play} from "svelte-heros-v2";
	import type { UserTask } from "../../models/UserTask.model";
	import { colorJobStatus } from "$lib/app";

    let open: boolean;

    let userTask: UserTask;

    export function init(_userTask: UserTask) {
        userTask = _userTask;
        open = true;
    }
</script>

<Modal title="User task properties" bind:open={open} autoclose class="w-full">
    <Table divClass='relative overflow-x-auto border rounded-lg'>
        <TableHead>
            <TableHeadCell>Parameter</TableHeadCell>
            <TableHeadCell>Value</TableHeadCell>
        </TableHead>
        <TableBody>
            <TableBodyRow>
                <TableBodyCell>Process instance key</TableBodyCell><TableBodyCell><A href="/instances/{userTask.processInstanceKey}" class="font-medium hover:underline">{userTask.processInstanceKey}</A></TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Element Id</TableBodyCell><TableBodyCell>{userTask.elementId}</TableBodyCell>
            </TableBodyRow> 
            <TableBodyRow>
                <TableBodyCell>Status</TableBodyCell>
                <TableBodyCell>
                    <Badge color="{colorJobStatus[userTask.status]}"  rounded class="px-2.5 py-0.5">
                        <Indicator color="{colorJobStatus[userTask.status]}" size="xs" class="mr-1"/>{userTask.status}
                    </Badge>
                </TableBodyCell>            
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Due Date</TableBodyCell><TableBodyCell>{userTask.dueDate}</TableBodyCell>
            </TableBodyRow>                  
            <TableBodyRow>
                <TableBodyCell>Follow Up Date</TableBodyCell><TableBodyCell>{userTask.followUpDate}</TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Created</TableBodyCell><TableBodyCell>{userTask.timestamp}</TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Assignee</TableBodyCell><TableBodyCell>{userTask.assignee}</TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Candidate Groups</TableBodyCell><TableBodyCell>{userTask.groups}</TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>                
                <TableBodyCell>Candidate Users</TableBodyCell><TableBodyCell>{userTask.users}</TableBodyCell>
            </TableBodyRow>
            <TableBodyRow>
                <TableBodyCell>Variables</TableBodyCell><TableBodyCell>{userTask.variables}</TableBodyCell>
            </TableBodyRow>
        </TableBody>
    </Table>    
</Modal>