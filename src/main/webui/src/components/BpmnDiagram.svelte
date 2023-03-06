<script lang="ts">
    import { onDestroy, onMount } from 'svelte';

    import inherits from 'inherits';

    import Viewer from 'bpmn-js/lib/Viewer';

    import ZoomScrollModule from 'diagram-js/lib/navigation/zoomscroll';
    import MoveCanvasModule from 'diagram-js/lib/navigation/movecanvas';
    import {ViewfinderCircle} from "svelte-heros-v2";



    function CustomViewer(options) {
        Viewer.call(this, options);
    }

    inherits(CustomViewer, Viewer);

    CustomViewer.prototype._customModules = [
        ZoomScrollModule,
        MoveCanvasModule
    ];

    CustomViewer.prototype._modules = [].concat(
        Viewer.prototype._modules,
        CustomViewer.prototype._customModules
    );

    const noop = () => {};
    export let xml = '';

    export let instance;

    export let extend = false;

    export let onError = noop;
    export let onLoaded = noop;
    export let onWarning = noop;

    export function addElementSelectedMarker(elementId) {
        viewer.get('canvas').addMarker(elementId, 'bpmn-element-selected');
    }

    export function removeElementSelectedMarker(elementId) {
        viewer.get('canvas').removeMarker(elementId, 'bpmn-element-selected');
    }

    function resetView() {
        viewer.get('canvas').zoom('fit-viewport')
    }

    let container;
    let viewer;


    function addMarkers(viewer) {
        const canvas = viewer.get('canvas');
        const overlays = viewer.get('overlays');
        canvas.zoom('fit-viewport');

        if (!instance) {
            return;
        }
        if (!extend) {
            if (instance.elementInstances) {
                instance.elementInstances.forEach(e => {
                    const style = ((e.activeInstances > 0) ?
                        'inline-block whitespace-nowrap rounded-full bg-lime-400 px-2 py-1 text-center align-baseline text-[0.75em] font-bold leading-none text-white'
                        : 'inline-block whitespace-nowrap rounded-full bg-gray-400 px-2 py-1 text-center align-baseline text-[0.75em] font-bold leading-none text-white');
                    overlays.add(e.elementId, {
                        position: {top: -27, left: 0},
                        html: '<span class="' + style + '">' + e.activeInstances + ' | ' + e.endedInstances + ' </span>'
                    });
                });
            }
            return;
        }

        if (instance.activeActivities) {
            instance.activeActivities.forEach(e => canvas.addMarker(e, "bpmn-element-active"));
        }
        if (instance.incidentActivities) {
            instance.incidentActivities.forEach(e => canvas.addMarker(e, 'bpmn-element-incident'));
        }
        if (instance.incidents) {
            let icon = `<span title="incident">
                <svg fill="white" class="w-8 h-8" stroke="red" stroke-width="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z"></path>
                </svg></span>`
            instance.incidents.forEach(e => {
               if (!e.isResolved) {
                   overlays.add(e.elementId, {
                       position: {
                           top: -30,
                           right: 20
                       },
                       html: icon
                   });
               }
            });
        }
        const injector = viewer.get('injector');
        const elementRegistry = injector.get('elementRegistry');
        const graphicsFactory = injector.get('graphicsFactory');
        if (instance.takenSequenceFlows) {
            instance.takenSequenceFlows.forEach(e => {
                const element = elementRegistry.get(e);
                const gfx = elementRegistry.getGraphics(element);
                const color = 'rgb(132 204 22)';
                const di = element && element.di;
                di.set('stroke', color);
                di.set('fill', color);
                graphicsFactory.update('connection', element, gfx);
            });
        }
        if (instance.completedActivities) {
            instance.completedActivities.forEach(e => canvas.addMarker(e, 'bpmn-element-completed'));
        }
    }

    function addToolTip(viewer) {

        if (!instance.bpmnElementInfos) {
            return;
        }

        const eventBus = viewer.get("eventBus");
        let infoOverlayId;
        const overlays = viewer.get("overlays");

        let bpmnElementInfo = {};
        instance.bpmnElementInfos.forEach(e => bpmnElementInfo[e.elementId] = e.info);


        eventBus.on("element.hover", function(e) {
            let elementId = e.element.id;
            let info = bpmnElementInfo[elementId];
            if (info) {
                infoOverlayId = overlays.add(elementId, {
                    position: {
                        bottom: -5,
                        left: 0
                    },
                    html: '<div role="tooltip" data-tooltip="true" class="whitespace-nowrap p-2 bg-white  rounded-lg border border-gray-200 shadow-md z-10 outline-none text-sm font-light">' + info + '</div>'
                });
            }
        });

        eventBus.on("element.out", function(e) {
            if (infoOverlayId) {
                overlays.remove(infoOverlayId);
            }
        });
    }

    onMount(async () => {
        viewer = new CustomViewer({
            container: container,
            keyboard: { bindTo: document }
        });
        viewer.on('import.done', event => {
            const { error, warnings } = event;
            if (error) {
                return onError(error);
            }
            if (warnings && warnings.length) {
                onWarning(warnings);
            }

            addMarkers(viewer);
            addToolTip(viewer);
            return onLoaded(event);
        });
    });

    onDestroy(() => {
        viewer.destroy();
    });

    // re-import diagram whenever xml changes
    $: viewer && (xml && viewer.importXML(xml));
    $: instance && viewer && (xml && viewer.importXML(xml));

</script>

<style>

.bpmn-diagram {
    height: 550px;
}

</style>

<div class="relative">


<div class="bpmn-diagram" style="width: 100%" bind:this={container}>
    <style>
        .bpmn-element-active .djs-visual > :nth-child(1) {
            stroke: rgb(132 204 22) !important;
        }
        .bjs-breadcrumbs {
            display: none;
        }
        .bpmn-element-incident .djs-visual * {
            stroke: rgba(255, 0, 0, 1) !important;
            fill: rgba(255, 0, 0, 0.1) !important;
            /*stroke-width: 1px !important;*/
        }
        .bpmn-element-completed .djs-visual * {
            stroke: rgb(132 204 22) !important;
            stroke-width: 2px !important;
            fill: rgb(190 242 100) !important;
        }
        .bpmn-element-selected .djs-visual > :nth-child(1) {
            stroke: rgb(59 130 246) !important;
            stroke-dasharray: 5;
            stroke-width: 2px !important;
        }
        .bpmn-info {
            background-color: rgba(0, 123, 255, 255);
            color: White;
            border-radius: 5px;
            font-size: 12px;
            padding: 5px;
            min-height: 16px;
            width: 100px;
            text-align: center;
        }
    </style>
</div>
    <ViewfinderCircle  on:click={resetView} class=" absolute top-0 right-2 w-8 h-8 focus:outline-none"/>
</div>
