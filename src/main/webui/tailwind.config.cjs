const config = {
	content: [
		"./src/**/*.{html,js,svelte,ts}",
		"./node_modules/flowbite-svelte/**/*.{html,js,svelte,ts}",
	],

	theme: {
		theme: {
			extend: {
				fontFamily: {
					sans: ['Inter', 'sans-serif'],
				},
			},
		},
	},

	plugins: [
		require('flowbite/plugin')
	],
	darkMode: 'class',
};

module.exports = config;
