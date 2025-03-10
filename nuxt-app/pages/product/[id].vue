<script setup lang="ts">
const route = useRoute('product-id')
const router = useRouter()
const productId = route.params.id

const cartStore = useCartStore()
await cartStore.initializeCart()
const { data: product, error } = await useAsyncData<ProductDetailRes>(
	'products',
	() => $fetch(`/api/products/${productId}`),
	{
		transform(data) {
			if (window) {
				data.orderedQuantity = cartStore.getOrderedQuantityByProductId(
					data.id
				)
			}
			return data
		}
	}
)

if (error.value || !product.value || Array.isArray(product.value)) {
	throw createError({ status: 404, message: 'Not Found' })
}

const breadcrumbs: Breadcrumb[] = useCategoryStore().getBreadcrumbById(
	product.value.categoryId
)

let isFirstRender = product.value.orderedQuantity === undefined

const canAddToCart = computed<boolean>(() => {
	if (!product.value) {
		return false
	}

	if (isFirstRender) {
		return product.value.quantity > 0
	}
	if (product.value.orderedQuantity) {
		return product.value.orderedQuantity < product.value.quantity
	}
	return true
})

const { notification } = useNotification()

function addToCart() {
	const isSuccess = cartStore.addToCart(product.value!)
	if (isSuccess) {
		notification('Thêm vào giỏ hàng thành công')
	} else {
		notification('Không còn hàng để bán', {
			isSuccess: false
		})
	}
}

function buyNow() {
	cartStore.addToCart(product.value!)
	router.push('/cart')
}

onMounted(() => {
	if (!isFirstRender) {
		return
	}
	isFirstRender = false

	if (product.value) {
		product.value = {
			...product.value,
			orderedQuantity: cartStore.getOrderedQuantityByProductId(
				product.value.id
			)
		}
	}
})
</script>

<template>
	<AppBreadcrumb :breadcrumbs="breadcrumbs" />

	<section class="container" v-if="product && !Array.isArray(product)">
		<div class="product-detail">
			<div class="row">
				<div class="col-9">
					<div class="row">
						<div class="col-4/9">
							<ProductDetailImgList />
						</div>
						<div class="col-5/9">
							<div class="product-detail-info">
								<div class="detail-header">
									<h2 class="detail-header-title">
										{{ product.name }}
									</h2>

									<div class="detail-header-info">
										<div class="info-id">
											Mã SP:
											<span class="info-id-value">
												{{ product.id }}
											</span>
										</div>

										<div class="info-interact">
											<div class="info-interact-rate">
												<div class="rate-icon">
													<i
														class="icon-star star-5"
													></i>
												</div>

												<p class="rate-label">
													({{ 0 }} đánh giá)
												</p>
											</div>
											<p class="info-interact-view">
												Lượt xem:
												<span class="view-value">{{
													product.viewCount
												}}</span>
											</p>
										</div>
									</div>
								</div>

								<div class="detail-body">
									<ProductDetailSpecList
										v-if="product.specifications"
										:specifications="product.specifications"
									/>
									<ProductDetailRelateList
										v-if="product.relatedProducts"
										:related-products="
											product.relatedProducts
										"
										:active-id="product.id"
									/>
									<LazyProductDetailPrice
										:original-price="product.originalPrice"
										:sale-price="product.salePrice"
									/>
								</div>

								<div class="detail-footer">
									<div
										class="detail-footer-action-list"
										v-if="canAddToCart"
									>
										<button
											class="action-item primary"
											@click="buyNow"
										>
											Mua ngay
										</button>
										<button
											class="action-item secondary"
											@click="addToCart"
										>
											Thêm vào giỏ hàng
										</button>
									</div>
									<div
										class="detail-footer-action-list"
										v-else
									>
										<p
											v-if="product.quantity <= 0"
											class="message error"
										>
											Đã hết hàng
										</p>
										<p v-else class="message warning">
											Đã mua số lượng tối đa
										</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-3">
					<ProductDetailSupportList />
				</div>
			</div>
		</div>
	</section>
</template>

<style lang="scss">
@use '@/assets/scss/pages/product-detail.scss';
</style>
