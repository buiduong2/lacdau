<script setup lang="ts">
import { useField } from 'vee-validate'

const props = defineProps<{ src?: string; fieldName: string }>()

const imageSrc = ref<string | undefined>(props.src)
const input = useTemplateRef<HTMLInputElement>('input-ref')
const urlComp = useCreateImageURL()

const fMainImage = useField(props.fieldName)

function handleChangeUpload() {
  if (input.value && input.value.files?.[0]) {
    const file = input.value.files[0]
    const url = urlComp.createUrl(file)
    fMainImage.setValue(file)
    imageSrc.value = url.src

    input.value.value = ''
  }
}
</script>
<template>
  <AppFormGroup title="Hình ảnh chính" desc="Hình ảnh sẽ được hiện thị trong danh sách sản phẩm">
    <div class="size-52 mx-auto relative">
      <img
        v-if="imageSrc"
        class="max-h-full max-w-full size-full cursor-pointer"
        :src="imageSrc"
        @click="input?.click()"
      />
      <Button v-else type="button" class="size-full" variant="outline" @click="input?.click()">
        <Icon icon="lucide:upload" class="h-4 w-4 text-muted-foreground" />
        <span class="sr-only">Upload</span>
      </Button>
      <input type="file" ref="input-ref" accept="image/*" hidden @change="handleChangeUpload" />
    </div>
  </AppFormGroup>
</template>
<style scoped></style>
