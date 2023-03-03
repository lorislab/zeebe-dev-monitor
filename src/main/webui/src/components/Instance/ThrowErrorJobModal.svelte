<script lang="ts">
    import {Button, Input, Label, Modal} from "flowbite-svelte";
    import {Bolt} from "svelte-heros-v2";

    let open;

    let message;
    let errorCode;
    let job;

    export function init(_job) {
        job = _job;
        message = 'Throw error by user.';
        errorCode = '';
        open = true;
    }

    async function throwErrorJob() {
        const res = await fetch('/api/job/' + job.key + "/throw-error", {
            method: 'PUT', headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                errorMessage: message,
                errorCode: errorCode
            })
        }).then((res) => {
            open = false;
        });
    }

</script>

<Modal bind:open={open} autoclose>
    <div >
        <Bolt class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" size="60"/>
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Throw error for '{job.jobType}' job</h3>
        <Label class="space-y-2 mb-2">
            <span>Error message</span>
            <Input type="text" bind:value={message} id="message" placeholder="Error message ..." name="message"/>
        </Label>
        <Label class="space-y-2 mb-2">
            <span>Error code</span>
            <Input type="text" bind:value={errorCode} id="code"/>
        </Label>
        <Button color="red" class="mr-2" on:click={throwErrorJob}>Throw Error</Button>
        <Button color='alternative'>No, cancel</Button>
    </div>
</Modal>