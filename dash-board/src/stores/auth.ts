import { Authority, type AccessTokenBody, type Profile, type TokenRes } from '@/types/auth/resTypes'
import { AxiosService } from '@/utils/axios-service'
import { generateOAuthProfileUrl } from '@/utils/security'
import { TokenService } from '@/utils/token-service'
import { acceptHMRUpdate, defineStore } from 'pinia'

export const useAuthStore = defineStore('auth-store', () => {
  const isAuthenticated = ref<boolean>(false)
  let routeToGo: { value: string } = { value: '/' }
  const authorities = ref<Authority[]>([])
  const isAuthorized = computed<boolean>(() => Boolean(authorities.value.length))

  const username = ref<string>()
  const profile = ref<Profile>()
  let tokenService: TokenService

  async function updateProfile() {
    const { data } = await fetchProfile()
    profile.value = data
  }

  async function login(
    code: string,
    verifier: string,
  ): Promise<{ success: boolean; routeToGo: string }> {
    let success = true

    try {
      const tokenRes: TokenRes = await fetchAccessToken(code, verifier)
      TokenService.createInstance(tokenRes, fetchRefreshAccessToken)
      AxiosService.createInstance(TokenService.getInstance())

      tokenService = TokenService.getInstance()
      const decodedBody: AccessTokenBody = parseAccessToken(tokenRes.access_token)
      authorities.value = decodedBody.authorities.map((authority) => Authority[authority])
      username.value = decodedBody.sub
      isAuthenticated.value = true
      updateProfile()
      const res = await fetchEmployeeCheck()
      if (!res.data) {
        return {
          success,
          routeToGo: '/profile/register',
        }
      } else {
        return {
          success,
          routeToGo: routeToGo.value,
        }
      }
    } catch (error) {
      console.log(error)
      success = false
    }

    return {
      success,
      routeToGo: routeToGo.value,
    }
  }

  async function logout() {
    await revokeRefreshToken(await tokenService.getRefreshToken())
    const url: string = await tokenService.getIdToken()
    window.location.href = generateOauthLogoutUrl(url)
  }

  function changePassword() {
    openOAuthPopup(generateOAuthChangePasswordUrl())
  }

  function getProfile() {
    openOAuthPopup(generateOAuthProfileUrl())
  }

  function getPermissions() {
    openOAuthPopup(generateOAuthPermissionsUrl())
  }

  return {
    logout,
    login,
    changePassword,
    getProfile,
    getPermissions,
    isAuthorized,
    isAuthenticated,
    routeToGo,
    authorities,
    profile,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useAuthStore, import.meta.hot))
}
