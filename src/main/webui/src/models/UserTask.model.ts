export type UserTask = {
    key: number
    jobType: string
    status: string
    worker: string
    retries: number
    elementId: string
    timestamp: string
    elementInstanceKey: number
    processInstanceKey: number
    errorCode: string
    errorMessage: string
    groups: string
    users: string
    assignee: string
    dueDate: string
    followUpDate: string
    isActivatable: boolean
    searchTerms: string
    variables: string
}