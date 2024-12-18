import { mockNuxtImport } from '@nuxt/test-utils/runtime'
import { mount } from '@vue/test-utils'
import _ from 'lodash'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import CategoryPagination from '~/components/CategoryPagination.vue'
const { useRouteMock, useRouterMock, pushMock } = vi.hoisted(() => {
	const pushMock = vi.fn(() => '/')

	return {
		useRouteMock: vi.fn((): any => {
			return { query: { page: 10 } }
		}),
		useRouterMock: vi.fn(() => {
			return { push: pushMock }
		}),
		pushMock
	}
})

mockNuxtImport('useRoute', () => {
	return useRouteMock
})

mockNuxtImport('useRouter', () => {
	return useRouterMock
})

beforeEach(() => {
	pushMock.mockReset()
})

describe('Normal render', () => {
	it('First render Correcly', async () => {
		useRouteMock.mockReturnValue({ query: {} })

		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage: 10
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')
		const activeBtns = wrapper.findAll('.active[data-test="btns"]')
		expect(useRouteMock).toHaveBeenCalledOnce()
		expect(useRouterMock).toHaveBeenCalledOnce()
		expect(wrapper.isVisible()).toBe(true)
		expect(btns).toHaveLength(7)
		expect(btns.map(btn => Number(btn.text()))).toEqual(_.range(1, 8))
		expect(activeBtns).toHaveLength(1)
		expect(activeBtns[0].text()).toBe('1')
	})
})

describe('Number of Button', () => {
	const maxCount = 7
	it('should render `maxCount` When `totalPage > maxCount` ', async () => {
		const currentPage = 3
		const totalPage = 10
		useRouteMock.mockReturnValue({ query: { page: currentPage } })
		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage: totalPage
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')
		expect(btns).toHaveLength(maxCount)
		expect(btns.map(btn => Number(btn.text()))).toEqual(
			_.range(1, maxCount + 1)
		)
	})

	it('should render totalPage Button correcly when totalPage < maxCount ', async () => {
		const currentPage = 3
		const totalPage = 5
		useRouteMock.mockReturnValue({ query: { page: currentPage } })
		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage: totalPage
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')
		expect(btns).toHaveLength(totalPage)
		expect(btns.map(btn => Number(btn.text()))).toEqual(
			_.range(1, totalPage + 1)
		)
	})

	it('should render totalPage || maxCount  when totalPage == maxCount ', async () => {
		const currentPage = 3
		const totalPage = maxCount
		useRouteMock.mockReturnValue({ query: { page: currentPage } })
		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage: totalPage
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')
		expect(btns).toHaveLength(totalPage)
		expect(btns.map(btn => Number(btn.text()))).toEqual(
			_.range(1, totalPage + 1)
		)
	})
})

describe('Page Query', async () => {
	it('should render 1 - 7 . And Active = 1. When query.page=undefined ', async () => {
		useRouteMock.mockReturnValue({ query: {} })
		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage: 10
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')
		expect(btns).toHaveLength(7)
		expect(btns.map(btn => Number(btn.text()))).toEqual([
			1, 2, 3, 4, 5, 6, 7
		])
	})

	it('should render currentPage = 2 . WHen currentPage = 2 ', async () => {
		const currentPage = 2
		useRouteMock.mockReturnValue({ query: { page: currentPage } })
		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage: 10
			}
		})

		const btns = wrapper.findAll('.active[data-test="btns"]')
		expect(btns).toHaveLength(1)
		expect(btns[0].text()).toBe(String(currentPage))
	})
})

describe('Page Count', () => {
	it('should render count btn', async () => {
		const currentPage = 3
		useRouteMock.mockReturnValue({ query: { page: currentPage } })
		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage: 10
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')
		expect(btns).toHaveLength(7)
		expect(btns.map(btn => Number(btn.text()))).toEqual([
			1, 2, 3, 4, 5, 6, 7
		])
	})

	it('Shoul render 3 left btn + active + 3 right Btn. WHen totalPage >  active > 3', async () => {
		const currentPage = 6
		useRouteMock.mockReturnValue({ query: { page: currentPage } })
		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage: 10
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')
		const activeBtns = wrapper.findAll('.active[data-test="btns"]')
		expect(btns).toHaveLength(7)
		expect(btns.map(btn => Number(btn.text()))).toEqual([
			..._.range(currentPage - 3, currentPage),
			currentPage,
			..._.range(currentPage + 1, currentPage + 4)
		])

		expect(activeBtns).toHaveLength(1)
		expect(activeBtns[0].text()).toBe(String(currentPage))
	})

	it('Render < 3 Left BTn And > 3 Right BTN . When active <= 3 ', async () => {
		const currentPage = 3
		useRouteMock.mockReturnValue({ query: { page: currentPage } })
		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage: 10
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')
		const activeBtns = wrapper.findAll('.active[data-test="btns"]')
		expect(btns).toHaveLength(7)
		expect(btns.map(btn => Number(btn.text()))).toEqual([
			..._.range(1, currentPage),
			currentPage,
			..._.range(currentPage + 1, 8)
		])

		expect(activeBtns).toHaveLength(1)
		expect(activeBtns[0].text()).toBe(String(currentPage))
	})

	it('render >3 left + Active + < 3. When active >= totalPage - 3 ', async () => {
		const currentPage = 7
		const totalPage = 10
		useRouteMock.mockReturnValue({ query: { page: currentPage } })
		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')
		const activeBtns = wrapper.findAll('.active[data-test="btns"]')
		expect(btns).toHaveLength(7)
		expect(btns.map(btn => Number(btn.text()))).toEqual([
			..._.range(totalPage - 6, currentPage),
			currentPage,
			..._.range(currentPage + 1, totalPage + 1)
		])

		expect(activeBtns).toHaveLength(1)
		expect(activeBtns[0].text()).toBe(String(currentPage))
	})
})

describe('Click', () => {
	it('Router Push a page query when Click btn', async () => {
		const currentPage = 7
		const totalPage = 10
		useRouteMock.mockReturnValue({ query: { page: currentPage } })

		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')

		let randomBtns

		do {
			randomBtns = _.sample(btns)!
		} while (randomBtns.text() === String(currentPage))

		await randomBtns.trigger('click')

		expect(pushMock).toHaveBeenCalledOnce()
		expect(pushMock).toHaveBeenCalledWith({
			query: { page: Number(randomBtns.text()) }
		})
	})

	it('Router Push queries, path Page when Click btn', async () => {
		const currentPage = 7
		const totalPage = 10

		const route = {
			query: {
				page: currentPage,
				brand: 'hello'
			},
			path: '/hello-world/'
		}

		useRouteMock.mockReturnValue(route)

		const wrapper = await mount(CategoryPagination, {
			props: {
				totalPage
			}
		})

		const btns = wrapper.findAll('[data-test="btns"]')

		let randomBtns

		do {
			randomBtns = _.sample(btns)!
		} while (randomBtns.text() === String(currentPage))

		await randomBtns.trigger('click')

		expect(pushMock).toHaveBeenCalledOnce()
		expect(pushMock).toHaveBeenCalledWith({
			...route,
			query: {
				...route.query,
				page: Number(randomBtns.text())
			}
		})
	})
})
