function addElementInstanceCounter(overlays, elementId, active, ended) {
    const style = ((active > 0) ? "bpmn-badge-active" : "bpmn-badge-inactive");
    overlays.add(elementId, { position: { top: -25, left: 0 },
        html: '<span class="' + style + '" data-toggle="tooltip" data-placement="bottom" title="active | ended">'
            + active + ' | ' + ended
            + '</span>'
    });
}

function addElementInstanceCompletedMarker(canvas, elementId) {
    canvas.addMarker(elementId, 'bpmn-element-completed');
}

function addElementInstanceActiveMarker(canvas, elementId) {
    canvas.addMarker(elementId, 'bpmn-element-active');
}

function addElementInstanceIncidentMarker(canvas, elementId) {
    canvas.addMarker(elementId, 'bpmn-element-incident');
}

function addElementSelectedMarker(elementId) {
    canvas.addMarker(elementId, 'bpmn-element-selected');
}

function removeElementSelectedMarker(elementId) {
    canvas.removeMarker(elementId, 'bpmn-element-selected');
}

function addIncidentMarker(overlays, elementId) {
    overlays.add(elementId, {
        position: {
            top: -25,
            right: 10
        },
        html: '<span class="bpmn-badge-incident" data-toggle="tooltip" data-placement="bottom" title="incident">!</span>'
    });
}

function markSequenceFlow(elementRegistry, graphicsFactory, flow) {
    const element = elementRegistry.get(flow);
    const gfx = elementRegistry.getGraphics(element);
    colorSequenceFlow(graphicsFactory, element, gfx, '#52b415');
}

function colorSequenceFlow(graphicsFactory, sequenceFlow, gfx, color) {
    const di = getDi(sequenceFlow);
    di.set('stroke', color);
    di.set('fill', color);
    graphicsFactory.update('connection', sequenceFlow, gfx);
}

function getDi(element) {
    return element && element.di;
}