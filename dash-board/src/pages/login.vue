<script setup lang="ts">
const router = useRouter()
const authStore = useAuthStore()
const isAuthenticating = ref<boolean>(false)
const error = ref<boolean>(false)
async function login() {
  const pkce = await generatePKCE()
  isAuthenticating.value = true
  const popup = openOAuthPopup(generateOAuthUrl(pkce.challenge))
  const interval = setInterval(async () => {
    if (popup?.closed) {
      clearInterval(interval)
      const code = getCode()
      if (code) {
        const { success, routeToGo } = await authStore.login(code, pkce.verifier)
        isAuthenticating.value = false
        if (success) {
          if (authStore.isAuthorized) {
            router.push(routeToGo)
          } else {
            router.push('/403')
          }
        } else {
          error.value = true
        }
      }

      isAuthenticating.value = false
    }
  }, 1000)
}
</script>

<template>
  <div class="w-full h-screen flex justify-center items-center flex-col gap-10">
    <h2 class="text-lg" v-if="error">Đăng nhập thất bại có lỗi xảy ra</h2>
    <Button size="lg" @click="login" :disable="isAuthenticating">
      {{ isAuthenticating ? 'Đang xử lý ...' : 'Bấm vào đây để đăng nhập' }}
    </Button>
  </div>
</template>

<style scoped></style>
