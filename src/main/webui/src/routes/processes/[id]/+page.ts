import type {PageLoad } from './$types';

export const load = (async ({ fetch, params }) => {
    const res = await fetch(`/api/process/${params.id}`);
    const process = await res.json();
    return { process };
}) satisfies PageLoad;
