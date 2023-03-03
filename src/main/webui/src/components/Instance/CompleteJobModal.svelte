<script lang="ts">
    import {Modal, Button, Textarea, Label} from "flowbite-svelte";
    import {Play} from "svelte-heros-v2";

    let open;

    let parameters;
    let job;

    export function init(_job) {
        job = _job;
        parameters = null;
        open = true;
    }

    async function completeJob() {
        let payload = {}
        if (parameters) {
            payload = JSON.parse(parameters)
        }
        const res = await fetch('/api/job/' + job.key + "/complete", {
            method: 'PUT', headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        }).then((res) => {
            open = false;
        });
    }
</script>

<Modal bind:open={open} size="xs" autoclose>
    <div >
        <Play class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" size="60"/>
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Complete '{job.jobType}' job</h3>
        <Label class="space-y-2 mb-2">
            <span>Parameters</span>
            <Textarea bind:value={parameters} id="parameters" placeholder={JSON.stringify({param1: "value", param2: 100})} rows="4" name="parameters"/>
        </Label>
        <Button class="mr-2" on:click={completeJob}>Complete</Button>
        <Button color='alternative'>No, cancel</Button>
    </div>
</Modal>