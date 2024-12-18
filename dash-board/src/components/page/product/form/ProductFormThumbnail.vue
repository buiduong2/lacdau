<script setup lang="ts">
interface Image {
  id?: number
  src: string
  alt: string
  file?: File
}
import type { ProductFull } from '@/types/products/resTypes'
import { useField, useFieldArray } from 'vee-validate'
const props = defineProps<{ product?: ProductFull }>()

const fMeta = useField<{ id?: number }[]>('imageData.thumbnails')
const fFile = useFieldArray('thumbnails')

const urlComp = useCreateImageURL()
const input = useTemplateRef('upload-input')
const images = ref<Image[]>([...(props.product?.detail?.thumbnails || [])])
if (images.value.length) {
  fMeta.setValue(images.value.map((i) => ({ id: i.id })))
}
const pickedIndexImage = ref<number>(0)
const viewedImage = computed<Image>(
  () => images.value[pickedIndexImage.value] || { alt: '', src: '/placeholder.svg' },
)
function clickUploadBtn() {
  input.value?.click()
}

function viewImage(index: number) {
  pickedIndexImage.value = index
}

function uploadImage() {
  if (input.value?.files?.[0]) {
    const file = input.value.files[0]
    fMeta.setValue([...(fMeta.value.value || []), {}])
    fFile.push(file)

    images.value.push({
      ...urlComp.createUrl(file),
      file: file,
    })
    if (input.value) {
      input.value.value = ''
    }
  }
}

function removeImage() {
  const image = images.value[pickedIndexImage.value]
  if (image) {
    urlComp.removeUrl(image.src)
    if (image.file) {
      const indexOfFile = fFile.fields.value.findIndex((field) => field.value === image.file)
      fFile.remove(indexOfFile)
    }

    fMeta.setValue(fMeta.value.value.filter((f, index) => index !== pickedIndexImage.value))

    images.value.splice(pickedIndexImage.value, 1)
  }
}
</script>

<template>
  <AppFormGroup
    title="Hình ảnh chi tiết"
    desc="Hãy thêm các hình ảnh sẽ hiển thị trong trang chi tiết tại đây"
  >
    <div class="grid gap-2">
      <div class="relative">
        <img
          :alt="viewedImage.alt"
          class="aspect-square w-full rounded-md object-cover"
          height="300"
          :src="viewedImage.src"
          width="300"
        />
        <Button
          v-if="images.length"
          class="absolute top-4 right-4"
          variant="default"
          type="button"
          @click="removeImage"
        >
          <Icon icon="lucide:x" />
          Xóa
        </Button>
      </div>
      <div class="grid grid-cols-3 gap-2">
        <div
          v-for="(image, index) in images"
          :class="index === pickedIndexImage ? 'border-foreground border-8' : ''"
          class="cursor-pointer"
        >
          <img
            @click="viewImage(index)"
            :alt="image.alt"
            class="aspect-square w-full rounded-md object-cover"
            height="84"
            :src="image.src"
            width="84"
          />
        </div>
        <button
          type="button"
          @click="clickUploadBtn"
          class="flex aspect-square w-full items-center justify-center rounded-md border border-dashed"
        >
          <Icon icon="lucide:upload" class="h-4 w-4 text-muted-foreground" />
          <span class="sr-only">Upload</span>
        </button>
        <input
          hidden
          id="upload-image"
          ref="upload-input"
          type="file"
          accept="image/*"
          @change="uploadImage"
        />
      </div>
    </div>
  </AppFormGroup>
</template>
