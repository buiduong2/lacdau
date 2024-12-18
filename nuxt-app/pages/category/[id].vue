<script lang="ts" setup>
const route = useRoute('category-id')
const categoryStore = useCategoryStore()
const category = categoryStore.getCategoryById(Number(route.params.id))

if (!category) {
	throw createError({ status: 404, message: 'Not Found' })
}
const breadcrumbs = categoryStore.getBreadcrumbById(category.id)
const { filters, query } = await useProductPageable(category.id, route)
const { data ,error} = await useFetch('/api/products', {
	query: query
})

if (!data || !data.value) {
	throw createError({ status: 404, message: 'Not Found' })
}

const viewMode = ref<'grid' | 'list'>('grid')
</script>
<template>
	<template v-if="category && data && filters">
		<AppBreadcrumb :breadcrumbs="breadcrumbs" />

		<section>
			<div class="container">
				<div class="row gy-0">
					<div class="col-25">
						<CategoryFilter :filters="filters" />
					</div>

					<div class="col-95">
						<div class="product-show-wrapper">
							<h2 class="page-title">
								{{ category.name }}
								<span class="quantity">
									({{ data.page.totalElements }} Sản phẩm)
								</span>
							</h2>
							<CategorySlide
								:categories="category.children"
								v-if="category.children"
							/>
							<div class="product-show">
								<CategoryAction v-model:view-mode="viewMode" />

								<CategoryProductList
									:products="data.content"
									:view-mode="viewMode"
								/>

								<CategoryPagination
									:total-page="data.page.totalPages"
								/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
	</template>
</template>
<style lang="scss">
@use '@/assets/scss/pages/product.scss';
</style>
