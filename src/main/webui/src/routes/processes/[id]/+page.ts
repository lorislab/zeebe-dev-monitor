import { error } from '@sveltejs/kit';
import type {PageLoad } from './$types';

export const load = (async ({ fetch, params }) => {
    const res = await fetch(`/api/process/${params.id}`);
    if (res.status != 200) {
        throw error(res.status, {message: `Process ${params.id} not found`});
    }        
    const process = await res.json();
    return { process };
}) satisfies PageLoad;
