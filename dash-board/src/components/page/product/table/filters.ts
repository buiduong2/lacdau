import type {
  TableFacetedFilterInput,
  TableFacetedFilterPick,
  TableFilterOption,
} from '../../app/table/types'

export async function getFilters() {
  interface Entity {
    id: number
    name: string
  }

  function toTableoption(entities: Entity[]): TableFilterOption[] {
    return entities.map((e) => ({
      label: e.name,
      value: String(e.id),
    }))
  }
  const attributeStore = useAttributeStore()
  await attributeStore.fetchInit()
  const brandStore = useBrandStore()
  await brandStore.fetchInit()
  const categoryStore = useCategoryStore()
  await categoryStore.fetchInit()

  const attrValFilters: TableFilterOption[] = toTableoption(attributeStore.attributeValues)
  const brandFilters: TableFilterOption[] = toTableoption(brandStore.brands)
  const categoryFilters: TableFilterOption[] = toTableoption(categoryStore.categoies)

  const filterInputs: TableFacetedFilterInput[] = [
    {
      label: 'Tên',
      name: 'search',
      placeholder: 'Sản phẩm A',
    },
  ]

  const filterPicks: TableFacetedFilterPick[] = [
    { columnId: 'categoryIds', options: categoryFilters, title: 'Category' },
    { columnId: 'brandIds', options: brandFilters, title: 'Brand' },
    { columnId: 'attributeValueIds', options: attrValFilters, title: 'Attribute Value' },
    {
      columnId: 'status',
      options: [
        { label: 'ACTIVE', value: 'ACTIVE' },
        { label: 'ARCHIVED', value: 'ARCHIVED' },
        { label: 'DRAFT', value: 'DRAFT' },
      ],
      title: 'status',
    },
  ]

  return {
    filterPicks,
    filterInputs,
  }
}
