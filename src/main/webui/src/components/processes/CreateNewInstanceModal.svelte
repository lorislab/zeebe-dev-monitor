<script lang="ts">

    import { page } from "$app/stores";
    import { Label, Modal, Textarea, Button } from "flowbite-svelte";

    let variables;

    export let open = false;

    async function createProcessInstance() {
        let tmp = {}
        if (variables) {
            tmp = JSON.parse(variables)
        }

        const res = await fetch('/api/process/' + $page.params.id, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(tmp)
        }).then((res) => {
            res.json();
            open = false;
        });
    }
</script>

<Modal title="Create new process instance" bind:open={open} size="xs" class="w-full">
    <form class="flex flex-col space-y-6">
        <Label class="space-y-2 mb-2">
            <span>Variables</span>
            <Textarea bind:value={variables} id="variables" placeholder={JSON.stringify({param1: "value", param2: 100})} rows="4" name="variables"/>
        </Label>
        <Button on:click={createProcessInstance}>Create</Button>
    </form>
</Modal>