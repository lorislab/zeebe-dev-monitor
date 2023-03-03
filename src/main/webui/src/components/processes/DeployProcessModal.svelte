<script lang="ts">
    import { Label, Modal, Fileupload, Button } from "flowbite-svelte";
    import {invalidate} from "$app/navigation";

    export let open = false;

    let files;

    async function deployProcess() {
        const formData = new FormData();
        formData.append('file', files[0]);
        formData.append('filename', files[0].name);

        const res = await fetch('/api/process', { method: 'POST', body: formData})
            .then((res) => {
                open = false;
            });
    }

</script>

<Modal title="Deploy new process" bind:open={open} size="xs" class="w-full">
    <form class="flex flex-col space-y-6">
        <Label class="space-y-2 mb-2">
            <span>Upload BPMN file</span>
            <Fileupload bind:files/>
        </Label>
        <Button on:click={deployProcess} disabled='{!files}'>Deploy</Button>
    </form>
</Modal>