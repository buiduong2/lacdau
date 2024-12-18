<script setup lang="ts">
import { ProductStatus } from '@/types/products/resTypes'
</script>
<template>
  <AppFormGroup title="Trạng thái của sản phẩm" desc="Khách hàng chỉ nhìn thấy sản phẩm ACTIVE">
    <AppFormField name="status" v-slot="componentField" label="Status" isRequired>
      <Select v-bind="componentField">
        <SelectTrigger class="w-full">
          <SelectValue placeholder="Chọn status" />
        </SelectTrigger>
        <SelectContent>
          <SelectItem v-for="option in ProductStatus" :key="option" :value="option">
            {{ option }}
          </SelectItem>
        </SelectContent>
      </Select>
    </AppFormField>
  </AppFormGroup>
</template>
<style scoped></style>
