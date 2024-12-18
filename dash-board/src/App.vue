<script setup lang="ts"></script>

<template>
  <!-- Sau khi có nhiều VIew ta nên thử xem tại sao lai jsuwr dụng susense ở bên trong RouterView ? Có lẽ nó chỉ render 1 lần duy nhất giữa các lần điều huowgns sẽ không được -->
  <AppLayout>
    <RouterView v-slot="{ Component, route }">
      <!-- Sử dụng default slot của Router View để tự xử lý Dinamic Component -->
      <Suspense v-if="Component" :timeout="100">
        <!-- Sử dụng Component để truyền vòa một render function -->
        <Component :is="Component" :key="route.name" />
        <!-- template Của suspsnse để hiện loading -->
        <template #fallback> Loading... </template>
      </Suspense>
    </RouterView>
  </AppLayout>
  <Toaster />
</template>

<style scoped></style>
