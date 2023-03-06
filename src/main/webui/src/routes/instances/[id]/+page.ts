import type {PageLoad } from './$types';

export const load = (async ({ fetch, params }) => {
    const res = await fetch(`/api/instance/${params.id}`);
    const instance = await res.json();
    return { instance };
}) satisfies PageLoad;
