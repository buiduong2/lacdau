<script setup lang="ts">
import { type CategoryTree } from '@/stores/category'
import { useField, type FormContext } from 'vee-validate'
const props = withDefaults(
  defineProps<{
    form: FormContext
    fieldName: string
    shoulDisplayTree?: <T>(tree: CategoryTree) => boolean
  }>(),
  { shoulDisplayTree: () => true },
)

const store = useCategoryStore()
await store.fetchInit()
const { setValue, errorMessage, value } = useField(props.fieldName)

const categoryTree: CategoryTree[] = store.categoryTrees

const pickedCategory: CategoryTree[] = findPath(categoryTree, (c) => c.id === value.value)

const pickedNeighbors: CategoryTree[][] = [categoryTree]

;(function getNeighborsFromPath() {
  for (let i = 0; i < pickedCategory.length; i++) {
    const category = pickedCategory[i]
    if (category.children) {
      pickedNeighbors.push(category.children)
    }
  }
})()

const pickedCategoryTree = ref<CategoryTree[][]>(pickedNeighbors)

function pickedItem(index: number, value: string) {
  const id = Number(value)
  setValue(id)
  if (pickedCategoryTree.value.length > index + 1) {
    pickedCategoryTree.value.splice(index + 1)
  }
  const node = pickedCategoryTree.value[index].find((node) => node.id === id)

  if (node?.children) {
    pickedCategoryTree.value.push(node.children)
  }
}
</script>

<template>
  <div class="grid grid-cols-3 gap-6">
    <div
      class="col-span-1 "
      v-for="(categories, index) in pickedCategoryTree"
      :key="categories[0].parent?.id || 0"
    >
      <div class="grid gap-3" v-if="shoulDisplayTree(categories[0])">
        <Label
          :for="`category-${index}`"
          :class="{
            'text-destructive': errorMessage,
          }"
          >{{ index === 0 ? 'Root Category' : 'Sub Category' }}
        </Label>
        <Select
          @update:model-value="(value) => pickedItem(index, value)"
          :default-value="pickedCategory[index] ? String(pickedCategory[index].id) : undefined"
        >
          <SelectTrigger :id="`category-${index}`" aria-label="Select category">
            <SelectValue placeholder="Select category" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem
              :value="String(category.id)"
              v-for="category in categories"
              :key="category.id"
            >
              {{ category.name }}
            </SelectItem>
          </SelectContent>
        </Select>
      </div v>
    </div>
    <p v-if="errorMessage" class="text-destructive col-span-3 text-sm">{{ errorMessage }}</p>
  </div>
</template>

<style scoped></style>
