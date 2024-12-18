<script setup lang="ts">
const route = useRoute('product-id')

const { data: product,error } = await useAsyncData('products', () =>
	$fetch(`/api/products/${route.params.id}`)
)

if (error.value || !product.value) {
	throw createError({ status: 404, message: 'Not Found' })
}
const breadcrumbs: Breadcrumb[] = useCategoryStore().getBreadcrumbById(
	product.value.categoryId
)
</script>

<template>
	<AppBreadcrumb :breadcrumbs="breadcrumbs" />

	<section class="container" v-if="product">
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
									<div class="detail-footer-action-list">
										<button class="action-item primary">
											Mua ngay
										</button>
										<button class="action-item secondary">
											Thêm vào giỏ hàng
										</button>
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
