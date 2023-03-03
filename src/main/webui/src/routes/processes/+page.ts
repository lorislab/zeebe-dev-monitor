import type {PageLoad} from './$types';

export const load = (async ({ fetch, depends }) => {
    const res = await fetch(`/api/process`);
    const items = await res.json();
    depends('app:processes');
    return { items };
}) satisfies PageLoad;