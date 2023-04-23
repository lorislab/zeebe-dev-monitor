<script lang="ts">
    import { Input, Textarea, Modal, Label, Button } from 'flowbite-svelte';

    async function sendMessage() {
        let payload = {}
        if (parameters) {
            payload = JSON.parse(parameters)
        }

        const res = await fetch('/api/message', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: name,
                correlationKey: key,
                payload: payload,
                timeToLive: duration
            })
        }).then((res) => {
            open = false;
        });
    }

    let key;
    let key_disabled = false;
    let name: string | null;
    let duration;
    let parameters;
    let editName = false;
    let open = false;

    export function init(_name: string | null, _key: string | null = null, _editName: boolean) {
        name = _name;
        editName = _editName;
        key = _key;
        key_disabled = _key != null;
        duration ="PT0S";
        parameters = null;
        open = true;
    }

</script>

<Modal title="Send message" bind:open={open} size="md" class="w-full">
    <form class="flex flex-col space-y-6">
        <div>
            <Label for="msg_name" class="mb-2">Message name</Label>
            <Input bind:value={name} type="text" id="msg_name" disabled='{!editName}' placeholder="Message name" />
        </div>
        <div>
            <Label for="msg_key" class="mb-2">Correlation key</Label>
            <Input bind:value={key} type="text" id="msg_key" required placeholder="Correlation key" disabled='{key_disabled}' />
        </div>
        <div>
            <Label for="msg_live" class="mb-2">Time to live (duration)</Label>
            <Input bind:value={duration} type="text" id="msg_live" required/>
        </div>
        <Label class="space-y-2 mb-2">
            <span>Parameters</span>
            <Textarea bind:value={parameters} id="parameters" placeholder={JSON.stringify({param1: "value", param2: 100})} rows="4" name="parameters"/>
        </Label>
        <Button on:click={sendMessage} disabled='{!key || !duration}'>Send</Button>
    </form>
</Modal>