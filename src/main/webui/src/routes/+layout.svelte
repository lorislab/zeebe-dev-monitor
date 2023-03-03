<script lang="ts">
	import '../app.postcss';
    import { page } from '$app/stores';
    import {Navbar, NavBrand, NavLi, NavUl, NavHamburger, Indicator} from 'flowbite-svelte';
    import {ArrowPath} from "svelte-heros-v2";
    import {invalidateAll} from "$app/navigation";
    import {onDestroy} from "svelte";
    import websocketStore from "svelte-websocket-store";
    import {SvelteToast} from "@zerodevx/svelte-toast";

    $: route = $page.route;
    $: params = $page.params;

    type ProcessEvent = {
        type: string
    }
    type InstanceEvent = {
        type: string
        processInstanceKey: number
        processDefinitionKey: number
    }
    type ClusterEvent = {
        type: string
        message: string
    }
    type ServerEvent = {
        type: string
        data: ProcessEvent | InstanceEvent | ClusterEvent
    }

    let refresh = false;
    export const notification = websocketStore("ws://" + location.host + "/ws/notification", null, []);

    const unsubscribe = notification.subscribe((event: ServerEvent) => {
        if (!event) {
            return;
        }

        let pi: InstanceEvent;

        switch (event.type) {
            case 'PROCESS':
                if (route.id === '/processes') {
                    invalidateAll();
                }
                break;
            case 'PROCESS_INSTANCE':
                pi = event.data as InstanceEvent;
                if (pi.type === 'CREATED') {
                    if ((route.id.startsWith('/processes/') && pi.processDefinitionKey == params.id) ||
                        (route.id === '/instances')) {
                        invalidateAll();
                    }
                } else {
                    if ((route.id.startsWith('/processes/') && pi.processDefinitionKey == params.id)) {
                        switch (pi.type) {
                            case 'REMOVED':
                                invalidateAll();
                                break;
                            case 'UPDATED':
                                refresh = true;
                                break;
                        }
                    } else if (route.id.startsWith('/instances/') && pi.processInstanceKey == params.id) {
                        switch (pi.type) {
                            case 'REMOVED':
                                invalidateAll();
                                break;
                            case 'UPDATED':
                                refresh = true;
                                break;
                        }
                    }
                }
                break;
        }
    })
    onDestroy(() => {
        unsubscribe();
    })

    function reloadData() {
        invalidateAll();
        refresh = false;
    }
</script>

<svelte:head>
    <title>Zeebe dev monitor</title>
</svelte:head>

<SvelteToast />

<Navbar let:hidden let:toggle
        navClass="px-2 sm:px-4 w-full z-20 top-0 left-0 border-b shadow-md">
  <NavBrand href="/">
    <svg class="w-10 h-10" fill="#0D8DBA" width="20" height="18" viewBox="0 0 20 18" xmlns="http://www.w3.org/2000/svg">
        <g transform="translate(-.0070066 -.0067338)">
            <g transform="matrix(.079892 0 0 .079892 .25449 .050574)">
                <path fill-rule="evenodd" clip-rule="evenodd" d="m121.8 0c-67.3 0-121.8 54.5-121.8 121.8 0 42.9 22.2 80.7 55.8 102.4 2.3-5.2 6.6-10.1 11.2-14.3l109.3-0.3c4.7 4.3 9.2 9.3 11.5 14.6 33.6-21.7 55.8-59.4 55.8-102.4 0-67.3-54.5-121.8-121.8-121.8zm91.6 137.3c-2.2 0.3-4.1-0.6-5.3-2.2-1.3-1.8-3.4-2.8-5.6-2.8h-2.1c0.2 2.1 0.3 4.2 0.3 6.3 0.1 22.9-10.3 44.1-28.2 58.5l-101 0.3c-18-14.4-28.5-35.5-28.5-58.4 0-2.3 0.1-4.5 0.3-6.7h-2.1c-2.2 0-4.3 1-5.6 2.8-1.2 1.6-3.1 2.5-5.3 2.2-2.8-0.4-4.9-3-4.9-5.8v-12.1c0-2.9 2-5.4 4.9-5.8 1.1-0.1 2.1 0 3.1 0.4 6.1-48.2 43.2-85.4 88.2-85.5 16.3 0 31.7 4.8 44.9 13.4l8.4-6.8c-1.3-2.3-1.3-5.3 0.4-7.6 2.2-3.1 6.6-3.9 9.8-1.6 3.1 2.2 3.9 6.6 1.6 9.8-1.5 2.2-4.1 3.2-6.6 2.9l-3.9 10.6c18.1 15.5 30.7 38.5 34.1 64.9 0.9-0.4 2-0.6 3.2-0.5 2.8 0.4 4.9 3 4.9 5.8v12.1c-0.1 2.8-2.2 5.4-5 5.8z"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="m121.8 54.7h-0.2c-40.3 0.1-74.1 26.9-83.8 62.9 1 0.6 2.2 0.9 3.4 0.9h4.6c9.2-32.1 39.6-55.7 75.8-55.8h0.2c36.2 0 66.7 23.7 75.9 55.8h4.7c1.2 0 2.4-0.3 3.4-0.9-9.7-36.1-43.7-62.9-84-62.9z"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="m144.8 94.2h-45.9c-24.2 0-43.9 19.6-43.9 43.8v4c0 24.2 19.6 43.8 43.8 43.8h45.9c24.2 0 43.8-19.6 43.8-43.8v-4c0.1-24.2-19.5-43.8-43.7-43.8zm-1.7 77.9c-6.6 2.4-15.8 3-22.8 3-5.7 0-9.9-0.4-10.3-0.4-1.9-0.2-3.3-1.9-3.2-3.8 0.2-1.9 1.9-3.3 3.8-3.2 0.2 0 19.5 1.7 30-2.2 1.8-0.7 3.8 0.3 4.5 2.1 0.8 1.8-0.2 3.8-2 4.5zm19-23.7c-4.5 4.8-10.7 7.4-17.3 7.4-7.8 0-15.1-3.8-19.5-10.2-1.1-1.6-0.7-3.8 0.9-4.9s3.8-0.7 4.9 0.9c3.2 4.5 8.3 7.2 13.8 7.2 4.7 0 9-1.9 12.2-5.3s4.8-7.8 4.6-12.5c-0.5-8.6-7.5-15.5-16.1-15.9-5.1-0.2-9.9 1.9-13.3 5.7-0.8 0.8-1.5 1.9-2.2 3.2l-10.3 19.2c-1.5 2.7-3.3 5-5.3 6.8-4.3 3.7-9.8 5.8-15.6 5.8-6.6 0-12.7-2.6-17.3-7.4-4.5-4.8-6.8-11.1-6.5-17.7 0.7-12.1 10.7-22 22.8-22.5 8.5-0.3 16.5 3.9 21 11 1 1.6 0.6 3.8-1.1 4.8-1.6 1-3.8 0.6-4.8-1.1-3.3-5.1-8.8-8-14.9-7.8-8.6 0.3-15.7 7.3-16.1 15.9-0.3 4.7 1.4 9.1 4.6 12.5s7.5 5.3 12.2 5.3c4 0 7.9-1.5 11-4.1 1.4-1.2 2.7-2.9 3.8-4.8l10.3-19.2c1-1.8 2-3.3 3.1-4.5 4.8-5.4 11.6-8.3 18.8-8 12.1 0.5 22.1 10.4 22.8 22.5 0.4 6.7-2 12.9-6.5 17.7z"/>
            </g>
        </g>
    </svg>
  </NavBrand>
    <div class="flex md:order-2">
        <ArrowPath class="w-6 h-6 focus:outline-none active:animate-spin {refresh ? 'animate-bounce' : ''}" on:click={reloadData}></ArrowPath>
        <a href="https://github.com/lorislab/zeebe-dev-monitor" target="_blank" rel="noreferrer" class="pl-4">
            <svg class="w-6 h-6" aria-hidden="true" viewBox="0 0 16 16" >
                <path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z"></path>
            </svg>
        </a>
        <NavHamburger on:click={toggle} />
    </div>
  <NavUl {hidden}>
    <NavLi href="/processes" active={route.id.startsWith('/processes')}>Processes</NavLi>
    <NavLi href="/instances" active={route.id.startsWith('/instances')}>Instances</NavLi>
    <NavLi href="/incidents" active={route.id === '/incidents'}>Incidents</NavLi>
    <NavLi href="/jobs" active={route.id === '/jobs'}>Jobs</NavLi>
    <NavLi href="/messages" active={route.id === '/messages'}>Messages</NavLi>
    <NavLi href="/errors" active={route.id === '/errors'}>Errors</NavLi>
  </NavUl>
</Navbar>

<div class="mx-auto pt-4 container">
    <slot />
</div>


