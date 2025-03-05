<script setup lang="ts">
const route = useRoute('/callback')
saveCode(String(route.query.code))

setTimeout(() => {
  window.close()
}, 500)
</script>
<template>
  <div class="w-full h-screen flex justify-center items-center">
    <h1 class="text-xl">
      Đăng nhập thành công.
      <br />
      Đang xử lý ...
    </h1>
  </div>
</template>

<style scoped></style>
