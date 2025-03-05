<script lang="ts" setup>
const router = useRouter()
const { logout, isAuthenticated } = useAuthStore()
if (!isAuthenticated) {
  router.push('/login')
}
</script>

<template>
  <section class="error">
    <div>
      <div class="error__icon flex justify-center"><Icon icon="lucide:triangle-alert" /></div>
      <h1 class="error__code">403</h1>
      <p class="error__msg">Bạn không có quyền truy cập vào đây</p>
      <div class="error-footer">
        <p class="error-footer__text">Có lẽ bạn nên khám phá trang web sản phẩm</p>
        <RouterLink to="/">
          <Button class="max-w-36"> Quay về trang chủ </Button>
        </RouterLink>
        <Button class="max-w-36" variant="destructive" @click="logout"> Thoát tài khoản </Button>
      </div>
    </div>
  </section>
</template>

<style scoped>
.error {
  @apply mx-auto flex justify-center items-center flex-1 p-10 text-center -mt-20 min-h-[90vh];
}

.error__icon {
  @apply text-7xl text-destructive;
}

.error__code {
  @apply font-extrabold text-7xl text-secondary;
}

.error__msg {
  @apply text-3xl font-extrabold text-primary;
}

.error-footer {
  @apply flex flex-col items-center justify-center gap-5 mt-6 font-light;
}

.error-footer__text {
  @apply text-lg text-muted-foreground;
}

p {
  @apply my-2;
}
</style>
