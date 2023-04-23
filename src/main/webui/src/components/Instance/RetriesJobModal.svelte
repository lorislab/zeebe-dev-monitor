<script lang="ts">
    import {Modal, Button, NumberInput, Label} from "flowbite-svelte";
    import {Play} from "svelte-heros-v2";
	import type { Job } from "../../models/Job.model";

    let open: boolean;

    let retries: number;
    let job: Job;

    export function init(_job: Job) {
        job = _job;
        retries = 1;
        open = true;
    }

    async function completeJob() {
        let payload = {}
        const res = await fetch('/api/job/' + job.key + "/retries", {
            method: 'PUT', headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                retries: retries
            })
        }).then((res) => {
            open = false;
        });
    }
</script>

<Modal bind:open={open} autoclose class="w-full">
    <div >
        <Play class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" size="60"/>
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Update retries '{job.jobType}' job</h3>
        <Label class="space-y-2 mb-2">
            <span>Retries</span>
            <NumberInput bind:value={retries} id="retries" name="retries" min='0'/>
        </Label>
        <Button class="mr-2" on:click={completeJob}>Update retries</Button>
        <Button color='alternative'>No, cancel</Button>
    </div>
</Modal>