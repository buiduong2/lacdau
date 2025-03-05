import { defineNuxtRouteMiddleware, navigateTo } from 'nuxt/app'
import { setCookie } from 'h3' // Import từ h3
const authUrlsMatchers: RegExp[] = [/^\/account(\/.*)?$/, /^\/cart$/]
const guestOnlyUrlMatchers: RegExp[] = [
	/\/login/,
	/\/password-recovery/,
	/\/register/
]

export default defineNuxtRouteMiddleware(async (to, from) => {
	if (import.meta.server) {
		const sessionId = useCookie('auth')

		const userDetails = await $fetch('/api/auth/details', {
			method: 'POST',
			body: { sessionId: sessionId.value }
		})

		const isLogged: boolean = Boolean(userDetails)
		if (isLogged) {
			const customerRegisteredCookie = useCookie('customer-registered')
			let isCustomerRegisterd = false
			if (!customerRegisteredCookie.value) {
				isCustomerRegisterd = await $fetch(
					'/api/customers/check-register',
					{
						headers: {
							Cookie: `auth=${sessionId.value}`
						}
					}
				)
				const event = useRequestEvent() // Lấy event của request

				setCookie(
					event!,
					'customer-registered',
					JSON.stringify(isCustomerRegisterd),
					{
						httpOnly: true,
						secure: true,
						sameSite: 'lax',
						path: '/'
					}
				)
			} else if (customerRegisteredCookie.value === 'false') {
				isCustomerRegisterd = false
			} else {
				isCustomerRegisterd = true
			}

			if (!isCustomerRegisterd && to.path !== '/welcome') {
				return navigateTo('/welcome')
			}
			if (isCustomerRegisterd && to.path === '/welcome') {
				return navigateTo('/account')
			}
		} else if (to.path === '/welcome') {
			return navigateTo('/')
		}

		let isAuthPage = false
		let isGuestOnlyPage = false

		if (authUrlsMatchers.some(matcher => matcher.test(to.path))) {
			isAuthPage = true
		} else if (
			guestOnlyUrlMatchers.some(matcher => matcher.test(to.path))
		) {
			isGuestOnlyPage = true
		}

		if (isAuthPage && !isLogged) {
			return navigateTo('/login')
		} else if (isGuestOnlyPage && isLogged) {
			return navigateTo('/account')
		}
	}

	if (import.meta.client) {
		if (to.path === '/cart') {
			const { isAuthenticated } = useAuthStore()
			if (!isAuthenticated) {
				return navigateTo('/login')
			}
		}
	}

	return
})
