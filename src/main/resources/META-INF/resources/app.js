// rest api

function handleErrors(response) {
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response;
}

function updateVariable(scopeKey, name, elementId) {
    const obj = {};
    obj[name] = getJSONObject(elementId);

    const requestOptions = { method: 'PUT', headers: { 'Content-Type': 'application/json; charset=utf-8' },
        body: JSON.stringify(obj)
    };

    fetch('/api/instance/' + scopeKey + '/variable', requestOptions)
        .then(handleErrors)
        .then(r => showSuccess("Variable updated."))
        .catch(e => showError(e));
}

function setVariable(keyId, nameId, valueId) {

    const scopeKeyElement = document.getElementById(keyId);
    const scopeKey = scopeKeyElement.options[scopeKeyElement.selectedIndex].value;
    const name = document.getElementById(nameId).value;

    const obj = {};
    obj[name] = getJSONObject(valueId);

    const requestOptions = { method: 'PUT', headers: { 'Content-Type': 'application/json; charset=utf-8' },
        body: JSON.stringify(obj)
    };

    fetch('/api/instance/' + scopeKey + '/variable?local=true', requestOptions)
        .then(handleErrors)
        .then(r => showSuccess("Variable set."))
        .catch(e => showError(e));
}


function cancelInstance(key) {
    const requestOptions = { method: 'DELETE', headers: { 'Content-Type': 'application/json; charset=utf-8' }};
    fetch('/api/instance/' + key, requestOptions)
        .then(handleErrors)
        .then(r => showSuccess("Instance canceled."))
        .catch(e => showError(e));
}

function createInstance(key, elementId) {
    const obj = getJSONObject(elementId);
    const requestOptions = { method: 'POST', headers: { 'Content-Type': 'application/json; charset=utf-8' },
        body: JSON.stringify(obj)
    };

    fetch('/api/process/' + key, requestOptions)
        .then(handleErrors)
        .then(r => showSuccess("New instance created."))
        .catch(e => showError(e));
}

function completeJob(jobKey, elementId) {
    const obj = getJSONObject(elementId);
    const requestOptions = { method: 'PUT', headers: { 'Content-Type': 'application/json; charset=utf-8' },
        body: JSON.stringify(obj)
    };
    fetch('/api/job/' + jobKey + "/complete", requestOptions)
        .then(handleErrors)
        .then(r => showSuccess("Job completed."))
        .catch(e => showError(e));
}

function failJob(jobKey, msgElementId, retriesElementId) {

    const obj = {
        errorMessage: document.getElementById(msgElementId).value,
        retries: document.getElementById(retriesElementId).value,
    };

    const requestOptions = { method: 'PUT', headers: { 'Content-Type': 'application/json; charset=utf-8' },
        body: JSON.stringify(obj)
    };
    fetch('/api/job/' + jobKey + "/fail", requestOptions)
        .then(handleErrors)
        .then(r => showSuccess("Job failed."))
        .catch(e => showError(e));
}

function throwError(jobKey, msgElementId, errorCodeElementId) {

    const obj = {
        errorMessage: document.getElementById(msgElementId).value,
        errorCode: document.getElementById(errorCodeElementId).value
    };

    const requestOptions = { method: 'PUT', headers: { 'Content-Type': 'application/json; charset=utf-8' },
        body: JSON.stringify(obj)
    };
    fetch('/api/job/' + jobKey + "/throw-error", requestOptions)
        .then(handleErrors)
        .then(r => showSuccess("Job throw error."))
        .catch(e => showError(e));

}

function resolveJobIncident(incidentKey, jobKey, retriesElementId) {
    const retries = document.getElementById(retriesElementId).value;
    resolveIncident(incidentKey, jobKey, retries);
}

function resolveProcessInstanceIncident(incidentKey) {
    resolveIncident(incidentKey, null, null);
}

function resolveIncident(key, jobKey, retries) {

    const requestOptions = { method: 'PUT', headers: { 'Content-Type': 'application/json; charset=utf-8' },
        body: JSON.stringify({ jobKey: jobKey, retries: retries })
    };
    fetch('/api/incident/' + key, requestOptions)
        .then(handleErrors)
        .then(r => showSuccess("Incident resolved."))
        .catch(e => showError(e));

}

function publishMessageSubscription(nameId, keyId, valueId, timeId ) {
    const data = {
        name: document.getElementById(nameId).value,
        correlationKey: document.getElementById(keyId).value,
        payload:  getJSONObject(valueId),
        timeToLive: document.getElementById(timeId).value
    };
    publishMessageWithPayload(data);
}

function publishMessageWithPayload(data) {
    const requestOptions = { method: 'POST', headers: { 'Content-Type': 'application/json; charset=utf-8' },
        body: JSON.stringify(data)
    };
    fetch('/api/message', requestOptions)
        .then(handleErrors)
        .then(r => showSuccess("Message published."))
        .catch(e => showError(e));
}

function getJSONObject(elementId) {
    const tmp = document.getElementById(elementId).value
    let obj = {};
    if (tmp) {
        obj = JSON.parse(tmp);
    }
    return obj;
}

// web-socket

let webSocket;

function wsConnect() {
    if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
        console.log("WebSocket is already opened.");
        return;
    }

    webSocket = new WebSocket("ws://" + location.host + "/ws/notification");
    webSocket.onopen = () => {
        console.log("Connected to the zeebe dev console web socket");
    }

    webSocket.onmessage = (event) => {
        let msg = JSON.parse(event.data)
        if (msg.type === 'PROCESS_INSTANCE') {
            let path = window.location.pathname;
            if (path.replace("/instance/", "") === msg.data.processInstanceKey.toString()) {
                showInfo('Process instance ' + msg.data.processInstanceKey + ' has changed.');
            } else if (path.replace("/process/", "") === msg.data.processDefinitionKey.toString()) {
                showInfo('Process instance(s) of this process ' + msg.data.processDefinitionKey + ' have changed.');
            }

        }
        if (msg.type === 'CLUSTER') {
            showError(msg.data.message)
        }
    }
}

function showInfo(msg) {
    showNotification('info', msg);
}

function showSuccess(msg) {
    showNotification('success', msg);
}

function showError(error) {
    showNotification('error', error);
}

function showNotification(type, msg) {
    const parent = document.getElementById('notification-' + type);
    const text = parent.querySelector('#notification-' + type + '-text');
    text.textContent = msg;
    parent.style.removeProperty("display");
}

function wsDisconnect() {
    webSocket.close();
}


function reload() {
    history.go(0)
}