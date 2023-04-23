<script lang="ts">
    import { Tabs, TabItem } from 'flowbite-svelte';
        import type { PageData} from './$types';

    import { InformationCircle, RectangleGroup, Envelope, Clock } from 'svelte-heros-v2';
    import ProcessInstances from "../../../components/processes/ProcessInstances.svelte";
    import BpmnDiagram from "../../../components/BpmnDiagram.svelte";
    import ProcessInfo from "../../../components/processes/ProcessInfo.svelte";
    import ProcessMessageSubscriptions from "../../../components/processes/ProcessMessageSubscriptions.svelte";
    import ProcessSignalSubscriptions from "../../../components/processes/ProcessSignalSubscriptions.svelte";
    import ProcessTimers from "../../../components/processes/ProcessTimers.svelte";

    export let data: PageData;

</script>

<BpmnDiagram xml={data.process.xml} instance={data.process.instance} />

<Tabs style="underline" contentClass='p-4 rounded-lg mt-4'>
    <TabItem open>
        <div slot="title" class="flex items-center gap-2"><InformationCircle class="w-5 h-5 focus:outline-none" />Info</div>
        <ProcessInfo info={data.process.info} />
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><RectangleGroup class="w-5 h-5 focus:outline-none"/>Instances</div>
        <ProcessInstances />
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><Envelope class="w-5 h-5 focus:outline-none" />Message subscriptions</div>
        <ProcessMessageSubscriptions />
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><Envelope class="w-5 h-5 focus:outline-none" />Signal subscriptions</div>
        <ProcessSignalSubscriptions />
    </TabItem> 
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><Clock class="w-5 h-5 focus:outline-none"/>Timers</div>
        <ProcessTimers />
    </TabItem>
</Tabs>