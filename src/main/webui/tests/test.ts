import { expect, test } from '@playwright/test';

test('index page has expected h1', async ({ page }) => {
	await page.goto('/');
	await expect(page.getByRole('heading', { name: 'Zeebe developer monitor' })).toBeVisible();
});

test('index page has expected title', async ({ page }) => {
	await page.goto('/');
	await expect(page).toHaveTitle('Zeebe dev monitor');
});