
export const colorSignalSubscriptionStatus = {
    DELETED: 'red',
    CREATED: 'green'
}

export const colorSignalStatus = {
    BROADCAST: 'blue',
    BROADCASTED: 'green'
}
export const colorTimerState = {
    CREATED: 'blue',
    TRIGGER: 'green',
    TRIGGERED: 'green',
    CANCEL: 'red',
    CANCELED: 'red'
}
export const colorIncidentState = {
    CREATED: 'red',
    RESOLVED: 'green'
}
export const colorJobStatus = {
    CREATED: 'blue',
    COMPLETE: 'green',
    COMPLETED: 'green',
    TIME_OUT: 'yellow',
    TIMED_OUT: 'yellow',
    FAIL: 'red',
    FAILED: 'red',
    UPDATE_RETRIES: 'dark',
    RETRIES_UPDATED: 'dark',
    CANCEL: 'indigo',
    CANCELED: 'indigo',
    THROW_ERROR: 'red',
    ERROR_THROWN: 'red',
    RECUR_AFTER_BACKOFF: 'red',
    RECURRED_AFTER_BACKOFF: 'purple',
}

export const colorCallProcessInstanceState = {
    COMPLETED: 'dark',
    TERMINATED: 'red',
    ACTIVE: 'green',
}
export const colorProcessInstanceState = {
    COMPLETED: 'dark',
    TERMINATED: 'red',
    ACTIVE: 'green',
}

export const colorMessageState = {
    PUBLISH: 'yellow',
    PUBLISHED: 'green',
    EXPIRE: 'pink',
    EXPIRED: 'red'
}

export const colorMessageSubscriptionState = {
    CREATING: 'blue',
    CREATE: 'blue',
    CREATED: 'blue',
    CORRELATING: 'green',
    CORRELATE: 'green',
    CORRELATED: 'green',
    REJECT: 'red',
    REJECTED: 'red',
    DELETE: 'dark',
    DELETED: 'dark',
    DELETING: 'dark'
}