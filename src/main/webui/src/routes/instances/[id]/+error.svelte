<script>
    import { page } from '$app/stores';
	import { Button } from 'flowbite-svelte';
    import {FaceFrown} from "svelte-heros-v2";
</script>

<section class="flex items-center h-full sm:p-16">
	<div class="container flex flex-col items-center justify-center px-5 mx-auto my-8 space-y-8 text-center">
		<FaceFrown size="60" class="text-gray-600 mr-4"/>
		<p class="text-3xl text-gray-600">{$page.error.message}</p>
        <Button href="/instances">Back to the instances page</Button>
	</div>
</section>