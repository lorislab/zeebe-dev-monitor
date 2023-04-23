<script lang="ts">
    import { Tabs, TabItem } from 'flowbite-svelte';
    import type { PageData} from './$types';
    import BpmnDiagram from '../../../components/BpmnDiagram.svelte';
    import {
        WrenchScrewdriver, Clock, InformationCircle, Envelope, PresentationChartBar, User,
        ExclamationTriangle, Share, ShieldExclamation, Squares2x2, AcademicCap, ArrowRightOnRectangle
    } from 'svelte-heros-v2';
    import InstanceInfo from "../../../components/Instance/InstanceInfo.svelte";
    import VariableTable from "../../../components/Instance/VariableTable.svelte";
    import InstanceAuditLog from "../../../components/Instance/InstanceAuditLog.svelte";
    import {page} from "$app/stores";
    import Incidents from "$components/Instance/Incidents.svelte";
    import InstanceMessageSubscriptions from "$components/Instance/InstanceMessageSubscriptions.svelte";
    import InstanceJobs from "$components/Instance/InstanceJobs.svelte";
    import InstanceTimers from "$components/Instance/InstanceTimers.svelte";
    import InstanceCallProcesses from "$components/Instance/InstanceCallProcesses.svelte";
    import InstanceErrors from "$components/Instance/InstanceErrors.svelte";
    import InstanceModify from "$components/Instance/InstanceModify.svelte";
	import InstanceEscalation from '$components/Instance/InstanceEscalation.svelte';
	import InstanceUserTask from '$components/Instance/InstanceUserTask.svelte';

    export let data: PageData;
    let diagram;

</script>

<BpmnDiagram bind:this={diagram} xml={$page.data.instance.xml}  instance={$page.data.instance} extend={true}/>

<Tabs style='underline' contentClass='mt-4'>
    <TabItem open>
        <div slot="title" class="flex items-center gap-2"><InformationCircle class="w-5 h-5 focus:outline-none" />Info</div>
        <InstanceInfo info={data.instance.detail} />
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><Squares2x2 class="w-5 h-5 focus:outline-none" />Variables</div>
        <VariableTable elementMouseOver={diagram.addElementSelectedMarker} elementMouseOut={diagram.removeElementSelectedMarker} />
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><Share class="w-5 h-5 focus:outline-none" />Audit log</div>
        <InstanceAuditLog elementMouseOver={diagram.addElementSelectedMarker} elementMouseOut={diagram.removeElementSelectedMarker}/>
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><ExclamationTriangle class="w-5 h-5 focus:outline-none" />Incidents</div>
        <Incidents elementMouseOver={diagram.addElementSelectedMarker} elementMouseOut={diagram.removeElementSelectedMarker}/>
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><AcademicCap class="w-5 h-5 focus:outline-none" />Jobs</div>
        <InstanceJobs elementMouseOver={diagram.addElementSelectedMarker} elementMouseOut={diagram.removeElementSelectedMarker}/>
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><User class="w-5 h-5 focus:outline-none" />User tasks</div>
        <InstanceUserTask elementMouseOver={diagram.addElementSelectedMarker} elementMouseOut={diagram.removeElementSelectedMarker}/>
    </TabItem>    
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><Envelope class="w-5 h-5 focus:outline-none" />Messages</div>
        <InstanceMessageSubscriptions elementMouseOver={diagram.addElementSelectedMarker} elementMouseOut={diagram.removeElementSelectedMarker} />
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><PresentationChartBar class="w-5 h-5 focus:outline-none" />Escalation</div>
        <InstanceEscalation elementMouseOver={diagram.addElementSelectedMarker} elementMouseOut={diagram.removeElementSelectedMarker} />
    </TabItem>    
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><Clock class="w-5 h-5 focus:outline-none" />Timers</div>
        <InstanceTimers elementMouseOver={diagram.addElementSelectedMarker} elementMouseOut={diagram.removeElementSelectedMarker} />
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><ArrowRightOnRectangle class="w-5 h-5 focus:outline-none" />Called instances</div>
        <InstanceCallProcesses elementMouseOver={diagram.addElementSelectedMarker} elementMouseOut={diagram.removeElementSelectedMarker} />
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><ShieldExclamation class="w-5 h-5 focus:outline-none" />Errors</div>
        <InstanceErrors />
    </TabItem>
    <TabItem>
        <div slot="title" class="flex items-center gap-2"><WrenchScrewdriver class="w-5 h-5 focus:outline-none" />Modify</div>
        <InstanceModify elementMouseOver={diagram.addElementSelectedMarker} elementMouseOut={diagram.removeElementSelectedMarker} />
    </TabItem>
</Tabs>