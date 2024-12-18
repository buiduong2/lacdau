<script setup lang="ts">
import { FlexRender } from '@tanstack/vue-table'
import { columns } from '../../categories/table/column'
import type { TableClientEmits, TableClientProps } from './types'

defineProps<TableClientProps>()

defineEmits<TableClientEmits>()
</script>

<template>
  <div class="space-y-4">
    <AppDataTableToolbar
      :table="table"
      @remove-selected-row="$emit('removeSelectedRow')"
      :filterPicks="filterPicks"
      :filter-inputs="filterInputs"
    />
    <div class="border rounded-md">
      <Table>
        <TableHeader>
          <TableRow v-for="headerGroup in table.getHeaderGroups()" :key="headerGroup.id">
            <TableHead>
              <Checkbox
                :checked="table.getIsAllPageRowsSelected()"
                @update:checked="(value) => table.toggleAllPageRowsSelected(!!value)"
                aria-label="Select all"
              />
            </TableHead>
            <TableHead v-for="header in headerGroup.headers" :key="header.id">
              <FlexRender
                v-if="!header.isPlaceholder"
                :render="header.column.columnDef.header"
                :props="header.getContext()"
              />
            </TableHead>
            <TableHead class="text-center">Action</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <template v-if="table.getRowModel().rows?.length">
            <template v-for="row in table.getRowModel().rows" :key="row.id">
              <TableRow :data-state="row.getIsSelected() ? 'selected' : undefined">
                <TableCell>
                  <Checkbox
                    :checked="row.getIsSelected()"
                    @update:checked="(value: boolean) => row.toggleSelected(!!value)"
                    aria-label="Select Row"
                  />
                </TableCell>
                <TableCell v-for="cell in row.getVisibleCells()" :key="cell.id">
                  <FlexRender :render="cell.column.columnDef.cell" :props="cell.getContext()" />
                </TableCell>
                <TableCell>
                  <div class="text-center">
                    <AppDataTableDropDown
                      :row="row.original"
                      @edit="(payload) => $emit('editById', payload)"
                      @clone="(payload) => $emit('cloneById', payload)"
                      @remove="(payload) => $emit('removeById', payload)"
                    />
                  </div>
                </TableCell>
              </TableRow>
            </template>
          </template>
          <template v-else>
            <TableRow>
              <TableCell :colspan="columns.length" class="h-24 text-center">
                No results.
              </TableCell>
            </TableRow>
          </template>
        </TableBody>
      </Table>
    </div>
    <AppDataTablePagination :table="table" />
  </div>
</template>
