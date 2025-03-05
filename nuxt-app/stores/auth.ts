import { defineStore } from 'pinia'
export const useAuthStore = defineStore('auth', () => {
	const router = useRouter()
	const isAuthenticated = ref<boolean>(false)
	const isAuthenticating = ref<boolean>(false)
	const profile = ref<Profile>()
	const authorities = ref<string[]>()

	function register() {
		registerWithPopup()
	}

	function changePassword() {
		changePasswordWithPopup()
	}

	function authInfo() {
		authInfoWithPopup()
	}

	async function login() {
		isAuthenticating.value = true
		try {
			const res = await loginWithPopUp()
			authorities.value = res.authorities
			profile.value = await $fetch('/api/auth/profile')
			isAuthenticated.value = true
			const isCustomerRegistered = await $fetch(
				'/api/customers/check-register'
			)
			if (isCustomerRegistered) {
				router.replace('/account')
			} else {
				router.replace('/welcome')
			}
		} catch (error) {
		} finally {
			isAuthenticating.value = false
		}
	}

	async function init() {
		if (!isAuthenticated.value) {
			const sessionId = useCookie('auth')
			if (sessionId.value) {
				const { data, error } = await useFetch('/api/auth/profile')

				if (data.value) {
					profile.value = data.value
					isAuthenticated.value = true
				}
			}
		}
	}

	async function logout() {
		const logoutUri: string = await $fetch('/api/oauth2/logout')
		window.location.href = logoutUri
	}

	return {
		isAuthenticated,
		isAuthenticating,
		profile,
		register,
		changePassword,
		authInfo,
		login,
		logout,
		init
	}
})
