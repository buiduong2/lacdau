<script setup lang="ts">
import type { AttributeValue } from '@/types/attribute-values/resTypes'
import { useFieldArray } from 'vee-validate'

const props = defineProps<{ oldAttributeValues: AttributeValue[] }>()

const { fields, remove, push } = useFieldArray<number>('removeAttributeValueId')

const attributeValues = computed(() =>
  props.oldAttributeValues.filter((av) => !fields.value.map((f) => f.value).includes(av.id)),
)

function getRemoveName(id: number) {
  return props.oldAttributeValues.find((av) => av.id === id)!.name
}
</script>

<template>
  <AppFormGroup
    title="Thuộc tính con có sẵn"
    desc="Xóa bớt các thuộc tính con có sẵn, lưu ý xóa có thể ảnh hưởng đến các liên kết có sẵn "
  >
    <div>
      <h4
        class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 mb-4"
      >
        Các thuộc tính đang có:
      </h4>
      <div class="flex gap-4 flex-wrap">
        <Button
          type="button"
          v-for="attr in attributeValues"
          :key="attr.id"
          variant="outline"
          @click="push(attr.id)"
          v-if="oldAttributeValues.length"
        >
          {{ attr.name }}
        </Button>
        <p v-if="oldAttributeValues.length === 0" class="text-sm italic text-center w-full">
          Không có thuộc tính nào có sẵn
        </p>
      </div>
    </div>
    <div>
      <h4
        class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 mb-4"
      >
        Các thuộc tính sẽ xóa:
      </h4>
      <div class="flex gap-4 flex-wrap">
        <Button
          type="button"
          variant="destructive"
          v-for="(field, index) in fields"
          :key="field.value"
          v-if="fields.length"
          @click="remove(index)"
        >
          {{ getRemoveName(field.value) }}
          <Icon icon="lucide:x" />
        </Button>
        <p v-if="fields.length === 0" class="text-sm italic text-center w-full">
          Chưa chọn thuộc tính nào để xóa
        </p>
      </div>
    </div>
  </AppFormGroup>
</template>

<style scoped></style>
