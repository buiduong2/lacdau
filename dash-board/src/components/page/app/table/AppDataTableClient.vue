<script setup lang="ts">
import { FlexRender } from '@tanstack/vue-table'
import type { TableClientProps } from './types'

defineProps<TableClientProps>()
</script>

<template>
  <div class="space-y-4">
    <AppDataTableToolbar
      :table="table"
      :action="action"
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
                    <AppDataTableDropDown :row="row.original" :action="action" />
                  </div>
                </TableCell>
              </TableRow>
            </template>
          </template>
          <template v-else>
            <TableRow>
              <TableCell :colspan="table.getAllColumns().length + 2" class="h-24 text-center">
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
