import type { TableAction } from '@/composables/useTableAction'
import type { Table } from '@tanstack/vue-table'
import type { Component } from 'vue'

export interface TablePageProps extends TableClientProps {
  heading: string
  description: string
  action: TableAction
}

export interface TableClientProps extends TableClientDropdown, TableClientToolbar {}

export interface TableClientDropdown {
  action: TableAction
}

export interface TableClientToolbar {
  filterPicks?: TableFacetedFilterPick[]
  filterInputs?: TableFacetedFilterInput[]
  action: TableAction
  table: Table<any>
}

export interface TableFilterOption {
  label: string
  value: string | null | number
  icon?: Component
}

export interface TableFacetedFilterPick {
  title: string
  columnId: string
  options: TableFilterOption[]
}

export interface TableFacetedFilterInput {
  placeholder: string
  name: string
  label: string
}
