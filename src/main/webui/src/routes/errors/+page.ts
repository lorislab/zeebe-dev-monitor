import type {PageLoad} from './$types';

export const load = (async ({ fetch }) => {
    const res = await fetch(`/api/errors`);
    const items = await res.json();
    return { items };
}) satisfies PageLoad;