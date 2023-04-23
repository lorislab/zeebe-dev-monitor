export type Job = {
    key: number
    jobType: string
    state: string
    worker: string
    errorCode: string
    errorMessage: string
    retries: number
    elementId: string
    elementInstanceKey: number
    processInstanceKey: number
    timestamp: string
    isActivatable: boolean
    variables: string
}