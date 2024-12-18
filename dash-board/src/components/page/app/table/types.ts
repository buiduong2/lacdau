import type { Table } from '@tanstack/vue-table'
import type { Component } from 'vue'
import type { RouteNamedMap } from 'vue-router/auto-routes'

export interface TablePageProps extends TableClientProps {
  heading: string
  description: string
  createRouteName: keyof RouteNamedMap
  updateRouteName: keyof RouteNamedMap
}

export interface TablePageEmits {
  (e: 'removeSelectedRow'): void
  (e: 'removeById', payload: any): void
}

export interface TableClientEmits {
  (e: 'editById', payload: any): void
  (e: 'cloneById', payload: any): void
  (e: 'removeById', payload: any): void
  (e: 'removeSelectedRow'): void
}

export interface TableClientProps {
  table: Table<any>
  filterPicks?: TableFacetedFilterPick[]
  filterInputs?: TableFacetedFilterInput[]
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
