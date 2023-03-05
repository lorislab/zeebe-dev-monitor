<script lang="ts">
    import {Modal, Button, Textarea, Label, Input} from "flowbite-svelte";
    import { Wrench} from "svelte-heros-v2";
    import {page} from "$app/stores";
    let open;

    let incident;
    let retries;

    export function init(_incident) {
        incident = _incident;
        retries = 0;
        open = true;
    }

    async function resolveIncident() {
        const res = await fetch('/api/incident/' + incident.key, {
            method: 'PUT', headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                jobKey: incident.jobKey,
                retries: retries
            })
        }).then((res) => {
            open = false;
        });
    }
</script>

<Modal bind:open={open} autoclose class="w-full">
    <div >
        <Wrench class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" size="60"/>
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Make sure you have fixed the error</h3>
        {#if incident.jobKey > 0}
            <Label class="space-y-2 mb-2">
                <span>Job retries</span>
                <Input type="number" bind:value={retries}></Input>
            </Label>
        {/if}
        <Label class="space-y-2 mb-2">
            <span>Error message</span>
            <Textarea rows="10" bind:value={incident.errorMessage} disabled></Textarea>
        </Label>
        <Button class="mr-2" on:click={resolveIncident}>Resolve</Button>
        <Button color='alternative'>No, cancel</Button>
    </div>
</Modal>