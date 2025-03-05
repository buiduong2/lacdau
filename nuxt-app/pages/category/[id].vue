<script lang="ts" setup>
const route = useRoute('category-id')
const categoryStore = useCategoryStore()
const category = categoryStore.getCategoryById(Number(route.params.id))

if (!category) {
	throw createError({ status: 404, message: 'Not Found' })
}
const breadcrumbs = categoryStore.getBreadcrumbById(category.id)
const { filters, query } = await useProductPageable(category.id, route)
const cartStore = useCartStore()
const { notification } = useNotification()
await cartStore.initializeCart()
let isFirstRender = true

const { data, error } = await useFetch('/api/products', {
	query: query,
	transform(data) {
		data.content.forEach(product => {
			product.mainImage = product.mainImage ?? {
				alt: 'Ảnh tạm',
				id: Math.random(),
				src: `https://picsum.photos/200/200?random=${Math.random()}`
			}
		})

		if (!isFirstRender) {
			updateOrderedProductQuantity(data.content)
		}

		return data
	}
})

if (error.value || !data.value) {
	throw createError({ status: 404, message: 'Not Found' })
}

function addToCart(product: ProductSummaryRes) {
	const success = cartStore.addToCart(product)
	if (success) {
		notification('Thêm sản phẩm vào giỏ hàng thành công')
	} else {
		notification('Số lượng sản phẩm thêm vào đã vượt quá số lượng có sẵn', {
			isSuccess: false
		})
	}
}

function updateOrderedProductQuantity(products: ProductSummaryRes[]) {
	products.forEach(product => {
		product.orderedQuantity = cartStore.getOrderedQuantityByProductId(
			product.id
		)
	})
}

onMounted(() => {
	if (data.value?.content) {
		updateOrderedProductQuantity(data.value.content)
	}
	isFirstRender = false
})

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
									@add-to-cart="addToCart"
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
