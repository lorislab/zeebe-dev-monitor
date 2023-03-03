<script lang="ts">
    import { Select, Label, Modal, Textarea, Button, Input} from "flowbite-svelte";

    let value;
    let name;
    let scope;

    export function init() {
        name = null;
        value = null;
        scope = null;
        if (activeScopes && activeScopes.length > 0) {
            scope = activeScopes[0].value;
        }
        open = true;
    }

    let open = false;
    export let activeScopes = [];

    async function setVariable() {
        let tmp = {}
        if (value) {
            tmp = JSON.parse(value)
        }
        let obj = {};
        obj[name] = tmp;

        const res = await fetch('/api/variables/' + scope + '?local=true', {
            method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(obj)
        }).then((res) => {
            res.json();
            open = false;
        });
    }

</script>

<Modal title="Create process instance variable" bind:open={open} size="xs" class="w-full">
    <form class="flex flex-col space-y-6">
        <div>
            <Label for="name" class="mb-2">Name</Label>
            <Input bind:value={name} type="text" id="name" placeholder="Name of the variable" required />
        </div>
        <div>
            <Label for="scope" class="mb-2">Scope</Label>
            <Select class="mt-2" items={activeScopes} bind:value={scope} placeholder="Active scopes..."/>
        </div>
        <Label class="space-y-2 mb-2">
            <span>Value</span>
            <Textarea bind:value={value} id="value" placeholder={JSON.stringify({param1: "value", param2: 100})} rows="4" name="value"/>
        </Label>
        <Button on:click={setVariable} disabled='{!name || !scope || !value}'>Create</Button>
    </form>
</Modal>