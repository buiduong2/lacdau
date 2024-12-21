<script setup lang="ts">

</script>
<template>
	<AppBreadcrumb
		:breadcrumbs="[{ href: '/account', id: 'random', name: 'Tài khoản' }]"
	/>
	<div class="section-account">
		<div class="container">
			<div class="row">
				<div class="col-3">
					<aside class="side-bar">
						<div class="side-bar-group">
							<h3 class="side-bar-group__title">
								Đơn hàng đặt mua
							</h3>
							<div class="side-bar-item">
								<NuxtLink
									class="side-bar__btn"
									active-class="current"
									to="/account/order-history"
								>
									Danh sách mua hàng
								</NuxtLink>
							</div>
						</div>
						<div class="side-bar-group">
							<h3 class="side-bar-group__title">
								Thông tin tài khoản
							</h3>
							<div class="side-bar-item">
								<NuxtLink
									class="side-bar__btn"
									active-class="current"
									to="/account/profile"
								>
									Thông tin cá nhân
								</NuxtLink>
							</div>
							<div class="side-bar-item">
								<NuxtLink
									class="side-bar__btn"
									active-class="current"
									to="/account/password"
								>
									Thay đổi mật khẩu
								</NuxtLink>
							</div>
						</div>
						<div class="side-bar-group">
							<div class="side-bar-item">
								<button class="side-bar__btn btn--logout">
									Log-out
								</button>
							</div>
						</div>
					</aside>
				</div>
				<div class="col-9">
					<NuxtPage />
				</div>
			</div>
		</div>
	</div>
</template>

<style lang="scss">
@use '@/assets/scss/pages/account.scss';
</style>
