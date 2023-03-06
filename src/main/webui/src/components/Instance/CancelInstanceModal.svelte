<script lang="ts">
    import {Modal, Button } from "flowbite-svelte";
    import {ExclamationCircle} from "svelte-heros-v2";
    import {page} from "$app/stores";
    export let open;

    async function cancelProcessInstance() {
        const res = await fetch('/api/instance/' + $page.params.id, {
            method: 'DELETE', headers: {'Content-Type': 'application/json'},
        }).then((res) => {
            open = false;
        });
    }
</script>

<Modal bind:open={open} size="xs" autoclose>
    <div class="text-center">
        <ExclamationCircle class="mx-auto mb-4 w-14 h-14 text-gray-400 dark:text-gray-200" size="60"/>
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">Are you sure you want to cancel this process instance?</h3>
        <Button color="red" class="mr-2" on:click={cancelProcessInstance}>Yes, I'm sure</Button>
        <Button color='alternative'>No, cancel</Button>
    </div>
</Modal>