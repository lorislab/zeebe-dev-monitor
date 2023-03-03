import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	server: {
		strictPort: true,
		port: 5175,
		host: '0.0.0.0',
		proxy: {
			"/api": {
				target: "http://127.0.0.1:8080",
				changeOrigin: true,
				secure: false,
				// rewrite: (path) => path.replace(/^\/api/, ""),
			},
			'/ws/notification': {
				target: 'ws://127.0.0.1:8080',
				changeOrigin: true,
				ws: true,
			},
		},
	},
	plugins: [sveltekit()]
});
