<script setup lang="ts">
withDefaults(defineProps<{ title?: string; groupId?: number }>(), {
  title: 'Tạo mới: Nhóm sản phẩm',
})
</script>
<template>
  <AppFormLayout :title="title">
    <form v-bind="$attrs" class="flex gap-6 flex-col">
      <AppFormGroup title="Tổng quan" desc="Nhập thông tin tổng quan">
        <AppFormFieldInput
          label="Tên"
          desc="Tên của nhóm để dễ dàng quản lý"
          placeholder="Nhóm A B C"
          is-required
          name="name"
          type="text"
        />
      </AppFormGroup>

      <AppFormGroupToggle
        title="Sản phẩm con"
        desc="Các sản phẩm cùng loại sẽ được hiển thị trong trang chi tiết"
        :default-open="!!groupId"
      >
        <RelateGroupFormRelateInfo :group-id="groupId" />
      </AppFormGroupToggle>

      <Button type="submit">Submit</Button>
    </form>
  </AppFormLayout>
</template>
<style scoped></style>
