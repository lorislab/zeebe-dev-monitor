<script lang="ts">
    import { Label, Modal, Textarea, Button, Input} from "flowbite-svelte";

    let value;
    let name;
    let scope;
    let open = false;

    export function init(s, n, v) {
        name = n;
        value = v;
        scope = s;
        open = true;
    }

    async function setVariable() {
        let tmp = null;
        if (value) {
            tmp = JSON.parse(value)
        }
        let obj = {};
        obj[name] = tmp;

        const res = await fetch('/api/variables/' + scope, {
            method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(obj)
        }).then((res) => {
            res.json();
            open = false;
        });
    }

</script>

<Modal title="Update process instance variable" bind:open={open} class="w-full">
    <form class="flex flex-col space-y-6">
        <div>
            <Label for="name" class="mb-2">Name</Label>
            <Input bind:value={name} type="text" id="name" disabled />
        </div>
        <div>
            <Label for="scope" class="mb-2">Scope</Label>
            <Input bind:value={scope} type="text" id="scope" disabled />
        </div>
        <Label class="space-y-2 mb-2">
            <span>Value</span>
            <Textarea bind:value={value} id="value" placeholder='variable value in JSON format' rows="4" name="value"/>
        </Label>
        <Button on:click={setVariable} disabled='{!name || !scope }'>Update</Button>
    </form>
</Modal>