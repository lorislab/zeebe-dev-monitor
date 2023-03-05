<script>
	import { A, P, Card, Heading } from "flowbite-svelte";
	import { ArrowTopRightOnSquare } from 'svelte-heros-v2';

</script>

<div class="text-center text-gray-800 py-20 px-6">
	<Heading tag="h1" class="mb-4" data-testid="index-title">Zeebe developer monitor</Heading>
	<P align="center" weight="light">
		It is designed for developers to get in touch with Zeebe and workflow execution (BPMN), test workflows manually, provide insides on how workflows are executed.
	</P>
	<div class="grid grid-flow-row-dense lg:grid-cols-3 md:grid-cols-2 sm:grid-cols-2 grid-rows-2 gap-4 pt-6">
		<Card>
			<Heading tag="h5" class="mb-2">Process definition</Heading>
			<P color="text-gray-500 dark:text-gray-400" align="center" weight="light">
				Processes are flowchart-like blueprints that define the orchestration of tasks. Every task represents a piece of business logic so the ordered execution produces a meaningful result.
				<A href="https://docs.camunda.io/docs/components/concepts/processes/" target="_blank" rel="noreferrer">
					Read more <ArrowTopRightOnSquare class="w-4 h-4 ml-2" />
				</A>
			</P>
		</Card>
		<Card>
			<Heading tag="h5" class="mb-2">Process instances</Heading>
			<P color="text-gray-500 dark:text-gray-400" align="center" weight="light">
				Depending on the process definition, an instance of it can be created in several ways.
				Camunda Platform supports the following ways to create a process instance
				<A href="https://docs.camunda.io/docs/components/concepts/process-instance-creation/" target="_blank" rel="noreferrer">
					Read more <ArrowTopRightOnSquare class="w-4 h-4 ml-2" />
				</A>
			</P>
		</Card>

		<Card>
			<Heading tag="h5" class="mb-2">Incidents</Heading>
			<P color="text-gray-500 dark:text-gray-400" align="center" weight="light">
				In Camunda Platform, an incident represents a problem in process execution. This means a
				process instance is stuck at a particular point, and requires user interaction to resolve the
				problem. <A href="https://docs.camunda.io/docs/components/concepts/incidents/" target="_blank" rel="noreferrer">
				Read more<ArrowTopRightOnSquare class="w-4 h-4 ml-2" />
				</A>
			</P>
		</Card>
		<Card>
			<Heading tag="h5" class="mb-2">Jobs</Heading>
			<P color="text-gray-500 dark:text-gray-400" align="center" weight="light">
				A job worker is a service capable of performing a particular task in a process. Each time such a task needs to be performed, this is represented by a job.
				<A href="https://docs.camunda.io/docs/components/concepts/job-workers/" target="_blank" rel="noreferrer" >
					Read more<ArrowTopRightOnSquare class="w-4 h-4 ml-2" />
				</A>
			</P>
		</Card>
		<Card>
			<Heading tag="h5" class="mb-2">Messages</Heading>
			<P color="text-gray-500 dark:text-gray-400" align="center" weight="light">
				Process instances can respond to incoming messages. Published messages must be mapped onto
				a process instance. This step is called message correlation.
				<A href="https://docs.camunda.io/docs/components/concepts/messages/" target="_blank" rel="noreferrer">
					Read more<ArrowTopRightOnSquare class="w-4 h-4 ml-2" />
				</A>
			</P>
		</Card>
		<Card>
			<Heading tag="h5" class="mb-2">Errors</Heading>
			<P color="text-gray-500 dark:text-gray-400" align="center" weight="light">
				In process automation, you often encounter deviations from the default scenario. One way to resolve these deviations is using a BPMN error event, which allows a process model to react to errors within a task.
				<A href="https://docs.camunda.io/docs/components/modeler/bpmn/error-events/" target="_blank" rel="noreferrer">
					Read more<ArrowTopRightOnSquare class="w-4 h-4 ml-2" />
				</A>
			</P>
		</Card>
	</div>


</div>
