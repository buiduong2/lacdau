<script setup lang="ts">
import type { FormContext } from 'vee-validate'
import { formartCurrency } from '@/utils'
import type { RelateGroupInfo } from '@/types/relate-groups/resTypes'
const props = defineProps<{ form: FormContext; productId?: string }>()

const relateStore = useRelateStore()
await relateStore.fetchInit()
const relateGroups = relateStore.relateGroups.map((g) => ({
  label: g.name,
  value: g.id,
}))

const pickedId = computed(() => props.form.values.relateInfo?.relateGroupId)

const relateGroupInfo = ref<RelateGroupInfo>()

watch(
  [pickedId],
  async () => {
    if (pickedId.value) {
      relateGroupInfo.value = await relateStore.fetchDetail(pickedId.value)
    } else {
      relateGroupInfo.value = undefined
    }
  },
  { immediate: true },
)
</script>

<template>
  <AppFormCombobox
    v-if="relateGroups"
    :command-list="relateGroups"
    name="relateInfo.relateGroupId"
    place-holder="Chọn Nhóm"
    command-place-holder="Điền tên Nhóm..."
    label="Nhóm"
    desc="Một nhóm các sản phẩm liên quan"
    is-required
  />

  <AppFormFieldInput
    type="text"
    name="relateInfo.name"
    placeholder="Product màu đỏ"
    label="Tên trong nhóm"
    desc="Tên vắt tắt để phần biệt giữa các sản phẩm trong nhóm"
    is-required
  />

  <div v-if="relateGroupInfo">
    <h4
      class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 mb-4"
    >
      Các sản phẩm đã có trong nhóm:
    </h4>
    <div class="grid grid-cols-4 gap-4">
      <Card
        v-for="relate in relateGroupInfo.relateInfos"
        class="p-4 flex flex-col items-center justify-center text-center"
        :class="relate.productId === productId ? 'border-primary' : ''"
      >
        <p>{{ relate.name }}</p>
        <p>{{ formartCurrency(relate.price) }}</p>
      </Card>
    </div>
  </div>
</template>
