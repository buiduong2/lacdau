import { mountSuspended } from '@nuxt/test-utils/runtime'
import { describe, expect, it } from 'vitest'
import AppBreadcrumb from '~/components/AppBreadcrumb.vue'

const breadcumbs: Breadcrumb[] = [
	{ id: 2, href: '/category/1', name: 'LÓT CHUỘT' },
	{ id: 3, href: '/category/2', name: 'LÓT CHUỘT CỠ 80X30' }
]

describe('BreadCumb', async () => {
	const comp = await mountSuspended(AppBreadcrumb, {
		props: { breadcrumbs: breadcumbs }
	})

	it('should render Root Correctly', () => {
		const rootLink = comp.get('[data-test="root-link"]')
		expect(rootLink.attributes('href')).toBe('/')
		expect(rootLink.text()).toBe('TRANG CHỦ')
	})

	it('should render other Link Correcly', () => {
		const otherLinks = comp.findAll('[data-test="links"]')
		expect(otherLinks).toHaveLength(2)
		otherLinks.forEach((link, index) => {
			expect(link.attributes('href')).toBe(breadcumbs[index].href)
			expect(link.text()).toBe(breadcumbs[index].name)
		})
	})
})
