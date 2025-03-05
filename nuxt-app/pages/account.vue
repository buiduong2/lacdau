<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'

const store = useAuthStore()
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
									:to="{
										name: 'account-order-status',
										params: { status: 'pending' }
									}"
								>
									<Icon icon="lucide:wallet" />
									<span>Chờ xác nhận</span>
								</NuxtLink>
							</div>
							<div class="side-bar-item">
								<NuxtLink
									class="side-bar__btn"
									active-class="current"
									:to="{
										name: 'account-order-status',
										params: { status: 'processing' }
									}"
								>
									<Icon icon="lucide:truck" />
									<span>Chờ lấy hàng</span>
								</NuxtLink>
							</div>
							<div class="side-bar-item">
								<NuxtLink
									class="side-bar__btn"
									active-class="current"
									:to="{
										name: 'account-order-status',
										params: { status: 'completed' }
									}"
								>
									<Icon icon="lucide:package-check" />
									<span> Đã hoàn thành </span>
								</NuxtLink>
							</div>
							<div class="side-bar-item">
								<NuxtLink
									class="side-bar__btn"
									active-class="current"
									:to="{
										name: 'account-order-status',
										params: { status: 'canceled' }
									}"
								>
									<Icon icon="lucide:circle-x" />
									<span> Đã hủy </span>
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
									to="/account/customer"
								>
									<Icon icon="lucide:user" />
									<span> Hồ sơ</span>
								</NuxtLink>
							</div>

							<div class="side-bar-item">
								<button
									class="side-bar__btn"
									@click="store.authInfo"
								>
									<Icon icon="lucide:users" />
									<span>Tài khoản</span>
								</button>
							</div>
							<div class="side-bar-item">
								<button
									class="side-bar__btn"
									@click="store.changePassword"
								>
									<Icon icon="lucide:lock" />
									<span>Bảo mật</span>
								</button>
							</div>
						</div>
						<div class="side-bar-group">
							<div class="side-bar-item">
								<button
									class="side-bar__btn btn--logout"
									@click="store.logout"
								>
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
