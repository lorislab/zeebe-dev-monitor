<script lang="ts">
    import {Button, Input, Label, Modal, NumberInput, Textarea} from "flowbite-svelte";
    import {NoSymbol} from "svelte-heros-v2";

    let open;

    let message;
    let retries;
    let job;

    export function init(_job) {
        job = _job;
        message = 'Failed job custom message by user.';
        retries = 0
        open = true;
    }

    async function failJob() {
        const res = await fetch('/api/job/' + job.key + "/fail", {
            method: 'PUT', headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                errorMessage: message,
                retries: retries
            })
        }).then((res) => {
            open = false;
        });
    }

</script>

<Modal bind:open={open} autoclose class="w-full">
    <div >
        <NoSymbol class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" size="60"/>
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Fail '{job.jobType}' job</h3>
        <Label class="space-y-2 mb-2">
            <span>Retries</span>
            <NumberInput bind:value={retries} id="retries" name="retries" min='0'/>
        </Label>
        <Label class="space-y-2 mb-2">
            <span>Error message</span>
            <Textarea bind:value={message} placeholder='Failed job custom error message ...' rows="4" name="message"/>
        </Label>

        <Button color="red" class="mr-2" on:click={failJob}>Fail job</Button>
        <Button color='alternative'>No, cancel</Button>
    </div>
</Modal>