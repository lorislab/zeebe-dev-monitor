<script lang="ts">
    import {Modal, Button, Textarea, Label, Input} from "flowbite-svelte";
    import {Play} from "svelte-heros-v2";
	import type { UserTask } from "../../models/UserTask.model";

    let open: boolean;

    let parameters: string | undefined;
    let userTask: UserTask;

    export function init(_userTask: UserTask) {
        userTask = _userTask;
        parameters = undefined;
        open = true;
    }

    async function completeUserTask() {
        let payload = {}
        if (parameters) {
            payload = JSON.parse(parameters)
        }
        const res = await fetch('/api/task/' + userTask.key + "/complete", {
            method: 'PUT', headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        }).then((res) => {
            open = false;
        });
    }
</script>

<Modal bind:open={open} autoclose class="w-full">
    <div >
        <Play class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" size="60"/>
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Complete User Task</h3>
        <div>
            <Label for="msg_assignee" class="mb-2">Assignee</Label>
            <Input type="text" id="msg_assignee" disabled='true' placeholder="Assignee" value={userTask.assignee}/>
        </div>        
        <div>
            <Label for="msg_groups" class="mb-2">Candidate Groups</Label>
            <Input type="text" id="msg_groups" disabled='true' placeholder="Candidate Users" value={userTask.groups}/>
        </div>
        <div>
            <Label for="msg_users" class="mb-2">Candidate Users</Label>
            <Input type="text" id="msg_users" disabled='true' placeholder="Candidate Users" value={userTask.users}/>
        </div>    
        <div>
            <Label for="msg_var" class="mb-2">Variables</Label>
            <Textarea type="text" id="msg_var" disabled='true' placeholder="Input variables" value={userTask.variables}/>
        </div>             
        <Label class="space-y-2 mb-4">
            <span>Parameters</span>
            <Textarea bind:value={parameters} id="parameters" placeholder={JSON.stringify({param1: "value", param2: 100})} rows="4" name="parameters"/>
        </Label>
        <Button class="mr-2" on:click={completeUserTask}>Complete</Button>
        <Button color='alternative'>No, cancel</Button>
    </div>
</Modal>