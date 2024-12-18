<script setup lang="ts">
import { useFieldArray, type FormContext } from 'vee-validate'
import type { CommandItem } from '../../app/form/type'
import type { AttributeValue } from '@/types/attribute-values/resTypes'

defineProps<{ form: FormContext }>()
const fieldName = 'attributeValueIds'

const store = useAttributeStore()
await store.fetchInit()
const attributes: CommandItem[] = store.attributes.map((a) => ({ label: a.name, value: a.id }))
const attributeValuesByAttrId = store.getAttrValGroupByAttrId()

const { fields, push, remove } = useFieldArray<number>(fieldName)

const selectedAttribute = ref<CommandItem>()
const attributeValues = ref<AttributeValue[]>([])
const pickedAttributeIds = ref<Map<number, AttributeValue>>(new Map())

if (fields.value.length) {
  const picked = store.getAttributeValuesByIdIn(fields.value.map((f) => f.value))
  picked.forEach((p) => pickedAttributeIds.value.set(p.id, p))
}

function pickAttribute(attr: AttributeValue) {
  if (pickedAttributeIds.value.has(attr.id)) {
    const index = fields.value.findIndex((entry) => entry.value === attr.id)
    remove(index)
    pickedAttributeIds.value.delete(attr.id)
  } else {
    push(attr.id)
    pickedAttributeIds.value.set(attr.id, attr)
  }
}

function removeAttribute(id: number) {
  const index = fields.value.findIndex((entry) => entry.value === id)
  remove(index)
  pickedAttributeIds.value.delete(id)
}

watch(
  [selectedAttribute],
  () => {
    if (selectedAttribute.value) {
      attributeValues.value = attributeValuesByAttrId[selectedAttribute.value.value]
    }
  },
  { immediate: false },
)
</script>
<template>
  <div>
    <h4
      class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 mb-4"
    >
      Chọn nhóm thuộc tính:
    </h4>
    <AppCombobox
      :command-list="attributes"
      :value="selectedAttribute?.value"
      @select-item="(item) => (selectedAttribute = item)"
      command-placeholder="Điền vào tên nhóm..."
      placeholder="Chọn nhóm Attribute"
      not-form
    />
  </div>

  <div v-if="selectedAttribute">
    <h4
      class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 mb-4"
    >
      Chọn thuộc tính
    </h4>
    <div class="flex gap-4 flex-wrap">
      <Button
        type="button"
        v-for="attr in attributeValues"
        :key="attr.id"
        :variant="pickedAttributeIds.has(attr.id) ? 'default' : 'outline'"
        @click="pickAttribute(attr)"
      >
        {{ attr.name }}
      </Button>
    </div>
  </div>
  <AppFormField
    :name="fieldName"
    label="Thuộc tính đã chọn"
    :desc="fields.length > 0 ? 'Bấm lại để bỏ chọn' : undefined"
    is-required
  >
    <div class="flex flex-wrap gap-4">
      <p v-if="fields.length === 0" class="text-sm italic text-center w-full">
        Bạn chưa chọn gì cả
      </p>
      <div v-else v-for="(field, index) in fields" :key="`${fieldName}-${field.key}`">
        <Button variant="outline" type="button" @click="removeAttribute(field.value)">
          {{ pickedAttributeIds.get(field.value)?.name }}
          <Icon icon="lucide:x" />
        </Button>
      </div>
    </div>
  </AppFormField>
</template>
