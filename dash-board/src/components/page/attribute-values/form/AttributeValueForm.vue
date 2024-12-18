<template>
  <AppFormLayout :title="pageTitle">
    <form v-bind="$attrs" class="flex gap-4 flex-col">
      <AppFormGroup title="Tông quan" desc="Nhập thông tin tổng quan">
        <AppFormFieldInput
          name="name"
          isRequired
          label="Tên"
          type="text"
          placeholder="Thuộc tính A"
        />
      </AppFormGroup>

      <AppFormGroupToggle
        name="attributeId"
        title="Nhóm thuộc tính"
        desc="Chọn nhóm thuộc tính để thuộc về"
        :defaultOpen="!!form.values.attributeId"
      >
        <AttributeValueFormAttribute />
      </AppFormGroupToggle>
      <Button type="submit">Xác nhận</Button>
    </form>
  </AppFormLayout>
</template>

<script setup lang="ts">
import type { AttributeValueUpdate } from '@/types/attribute-values/reqTypes'
import type { FormContext } from 'vee-validate'

defineProps<{ pageTitle: string; form: FormContext | FormContext<AttributeValueUpdate> }>()
</script>

<style scoped></style>
