<script lang="ts">
    import {
        Button,
        Checkbox,
        Select,
        Table,
        TableBody, TableBodyCell, TableBodyRow,
        TableHead,
        TableHeadCell,
        Textarea,
    } from "flowbite-svelte";
    import {page} from "$app/stores";
    import {
        ChevronUpDown,
        CursorArrowRays,
        Pencil,
        XMark
    } from "svelte-heros-v2";
    import MultiSelect from 'svelte-multiselect'
    import {writable} from "svelte/store";
    import ModifyInstanceModal from "$components/Instance/ModifyInstanceModal.svelte";
    import type {ActiveElement} from "$components/Instance/ModifyInstanceModal.svelte";

    export let elementMouseOver;
    export let elementMouseOut;

    let terminateActivityIds: number[] = [];

    $: selected = []

    const activeItems = $page.data.instance.activateActivities.map(item => {
        return { label: item.id, value: item.id};
    })

    let selectedActive = writable<ActiveElement[]>([]);
    const addActive = (e) => {
         selectedActive.update(data => {
             data.push({id: e.detail.option.value, ancestor: null, parameters: null});
             return data;
         });
    }
    const removeActive = (e) => {
        selectedActive.update(data => {
            let index = data.findIndex(item => item.id === e.detail.option.value);
            if (index != -1) {
                data.splice(index, 1);
            }
            return data;
        });
    }
    const removeAllActive = () => {
        selectedActive.set([])
    }

    let modifyModal = false;

    function test() {
        console.log(terminateActivityIds);
        console.log($selectedActive);
    }
</script>


<div class="flex flex-row mb-2">
    <div class="flex-auto"></div>
    <div>
        <Button size="xs" on:click={modifyModal.init(terminateActivityIds, $selectedActive)} color="{$page.data.instance.detail.isRunning ? 'red' : 'alternative'}" class="shadow-md"
                disabled='{!$page.data.instance.detail.isRunning || (terminateActivityIds.length <= 0 && $selectedActive.length <= 0)}'>
            <Pencil class="mr-2 -ml-1 w-5 h-5 focus:outline-none" variation="solid" />Modify instance
        </Button>
    </div>
</div>

<ModifyInstanceModal bind:this={modifyModal} />

{#if $page.data.instance.terminateActiveActivities.length > 0}
    <p class="mb-2">Select active elements to terminate</p>
    <div class="grid gap-6 md:grid-cols-4">
    {#each $page.data.instance.terminateActiveActivities as item}
        <Checkbox custom bind:group={terminateActivityIds} value={item}>
            <div on:mouseover={elementMouseOver(item.elementId)} on:mouseout={elementMouseOut(item.elementId)} on:focus on:blur
                    class="inline-flex whitespace-nowrap bg-white dark:bg-gray-800 text-gray-500 dark:text-gray-400 rounded-lg border peer-checked:outline peer-checked:outline-offset-0 peer-checked:outline-blue-500 border-gray-200 dark:border-gray-700 shadow-md flex max-w-sm flex-col p-1 sm:p-2 hover:text-gray-600 peer-checked:text-gray-600 hover:bg-gray-50">
                <div><CursorArrowRays class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex "/>{item.elementId}</div>
            </div>
        </Checkbox>
    {/each}
    </div>
    <div class="border-t mb-4 mt-4"></div>
{/if}


<MultiSelect bind:selected options={activeItems} outerDivClass="flex-auto" inputClass="!text-gray-600 !text-sm !p-2.5"
             ulSelectedClass="!text-sm" ulOptionsClass="!text-sm"
             liSelectedClass="!bg-blue-500 !text-white !m-1 !p-1" on:add={addActive} on:remove={removeActive} on:removeAll={removeAllActive} placeholder="Create new active elements">
    <span slot="expand-icon"><ChevronUpDown /></span>
    <span slot="remove-icon"><XMark class="w-4 h-4" /></span>
</MultiSelect>
<Table hoverable={true} divClass='relative mt-2 border rounded-lg'>
    <TableHead>
        <TableHeadCell>Element Id</TableHeadCell>
        <TableHeadCell>Ancestor Element</TableHeadCell>
        <TableHeadCell>Parameters</TableHeadCell>
    </TableHead>
    <TableBody tableBodyClass="divide-y">
        {#each $selectedActive as item}
            <TableBodyRow>
                <TableBodyCell><CursorArrowRays on:mouseover={elementMouseOver(item.id)} on:mouseout={elementMouseOut(item.id)} class="w-5 h-5 mr-2 -ml-1 focus:outline-none inline-flex"/>{item.id}</TableBodyCell>
                <TableBodyCell>
                    <Select bind:value={item.ancestor}>
                        {#each $page.data.instance.ancestorActivities as {key, elementId}}
                            <option value={key}>{elementId}</option>
                        {/each}
                    </Select>
                </TableBodyCell>
                <TableBodyCell><Textarea rows="2" placeholder={JSON.stringify({param1: "value", param2: 100})} bind:value={item.parameters}/></TableBodyCell>
            </TableBodyRow>
            {/each}
    </TableBody>
</Table>


