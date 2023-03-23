import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vitest/config';

export default defineConfig({
	plugins: [sveltekit()],
	test: {
		environment: 'happy-dom',
		include: ['{test,src/lib/components}/**/*.{test,spec}.{js,ts}']
	},
	build: {
		minify: false
	}
});
