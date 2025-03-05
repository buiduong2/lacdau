<script setup lang="ts">
import type { TableClientToolbar } from './types'

const props = defineProps<TableClientToolbar>()
const isFiltered = computed(() => props.table.getState().columnFilters.length > 0)

const keywords = ref<string[]>([])

function searchFilter(name: string, index: number) {
  props.table.getColumn(name)?.setFilterValue(keywords.value[index])
}

function resetFilter() {
  props.table.resetColumnFilters()
  keywords.value = []
}
</script>

<template>
  <div class="flex items-center justify-between">
    <Button
      v-if="table.getIsSomeRowsSelected() || table.getIsAllRowsSelected()"
      variant="destructive"
      @click="
        action.removeByIdIn(
          table.getSelectedRowModel().rows.map((row) => row.getValue('id') as string),
          () => table.toggleAllRowsSelected(false),
        )
      "
      >Xóa</Button
    >
    <div class="flex flex-1 items-center space-x-2">
      <form
        v-if="filterInputs"
        v-for="(search, index) in filterInputs"
        :key="search.name"
        @submit.prevent="searchFilter(search.name, index)"
        class="flex items-center gap-2"
      >
        <Label :for="search.name">{{ search.label }}:</Label>
        <Input
          :id="search.name"
          :placeholder="search.placeholder"
          class="h-8 w-[150px] lg:w-[250px]"
          v-model="keywords[index]"
        />
      </form>

      <AppDataTableFacetedFilter
        v-if="filterPicks"
        :options="filter.options"
        :column="table.getColumn(filter.columnId)!"
        :title="filter.title"
        v-for="filter in filterPicks"
      />

      <Button v-if="isFiltered" variant="secondary" @click="resetFilter"> Reset </Button>
    </div>
    <AppDataTableViewOptions :table="table" />
  </div>
</template>

<style scoped></style>
