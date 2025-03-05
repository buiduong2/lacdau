<template>
  <div class="mt-5">
    <AppFormGroup desc="Đơn hàng đã hủy thành công. Chúng ta chỉ có thể xem lại thông tin đơn hàng" title="Đã hủy">
      <div class="flex justify-center flex-col items-center my-10">
        <Icon icon="lucide:circle-x" class="size-40" color="red" />
        <p class="text-lg">Đơn hàng đã kết thúc</p>
      </div>
      <Button @click="$router.push({ name: '/orders/[id]/', params: { id: route.params.id } })">Xem thông tin chi
        tiết
        <Icon icon="lucide:info" />
      </Button>
      <Button variant="secondary"
        @click="$router.push({ name: '/orders/[id]/history', params: { id: route.params.id } })">Xem lịch sử tiến trình
        <Icon icon="lucide:git-fork" />
      </Button>
    </AppFormGroup>
  </div>
</template>

<script setup lang="ts">
const route = useRoute('/orders/[id]/process/completed');
</script>

<style scoped></style>
