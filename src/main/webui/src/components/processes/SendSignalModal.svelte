<script lang="ts">
    import { Input, Textarea, Modal, Label, Button } from 'flowbite-svelte';

    async function sendSignal() {
        let payload = {}
        if (parameters) {
            payload = JSON.parse(parameters)
        }

        const res = await fetch('/api/signal', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: name,
                variables: payload
            })
        }).then((res) => {
            open = false;
        });
    }

    let name: string | undefined;
    let parameters: string | undefined;
    let open = false;
    let editName = false;

    export function init(_name: string, _editName: boolean = true) {
        name = _name;
        editName = _editName;
        parameters = undefined;
        open = true;
    }

</script>

<Modal title="Send message" bind:open={open} size="md" class="w-full">
    <form class="flex flex-col space-y-6">
        <div>
            <Label for="msg_name" class="mb-2">Message name</Label>
            <Input bind:value={name} type="text" id="msg_name" disabled='{!editName}' placeholder="Signal name" />
        </div>
        <Label class="space-y-2 mb-2">
            <span>Parameters</span>
            <Textarea bind:value={parameters} id="parameters" placeholder={JSON.stringify({param1: "value", param2: 100})} rows="4" name="parameters"/>
        </Label>
        <Button on:click={sendSignal} >Broadcast signal</Button>
    </form>
</Modal>