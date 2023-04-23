import type {PageLoad } from './$types';
import { error } from '@sveltejs/kit';

export const load = (async ({ fetch, params }) => {
    const res = await fetch(`/api/instance/${params.id}`);
    if (res.status != 200) {
        throw error(res.status, {message: `Process instance ${params.id} not found`});
    }    
    const instance = await res.json();
    return { instance };
}) satisfies PageLoad;
