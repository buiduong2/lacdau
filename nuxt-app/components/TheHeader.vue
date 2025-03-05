<script setup lang="ts">
import { Icon } from '@iconify/vue'
import { salesPlatformLinks } from '~/mockData/header'

const isActivemMenu = ref(false)
const showCart = ref<boolean>(false)
const authStore = useAuthStore()
const cartStore = useCartStore()
await cartStore.initializeCart()
</script>

<template>
	<header class="section-header">
		<div class="top-group">
			<div class="container">
				<div class="row content-around">
					<div class="col-full">
						<div class="top-group-left">
							<ul class="btn-list">
								<li class="header-tooltip-wraper">
									<button class="btn">
										<i class="icon icon-map"></i> Địa chỉ
										liên hệ
									</button>
									<HeaderTooltipLocation />
								</li>
								<li class="header-tooltip-wraper">
									<button class="btn">
										<i class="icon icon-headphone"></i>
										Hotline trực tuyến
									</button>
									<HeaderTooltipContract />
								</li>
							</ul>

							<div class="sales-platform-link-list">
								<a
									class="platform-link"
									href="#"
									v-for="link in salesPlatformLinks"
									:key="link.href"
								>
									<i
										v-if="link.isCustomIcon"
										:class="link.icon"
									></i>
									<Icon v-else :icon="link.icon" />
									{{ link.title }}
								</a>
							</div>
						</div>
					</div>

					<div class="col">
						<div class="top-group-right">
							<a href="#" class="news-link">
								<i class="icon icon-news"></i> Tin tức</a
							>
							<div
								class="auth-list"
								v-if="!authStore.isAuthenticated"
							>
								<i class="icon icon-user"> </i>
								<a
									href="#"
									class="auth-action"
									@click.prevent="authStore.register"
								>
									Đăng ký
								</a>
								<span>/</span>
								<a
									href="#"
									class="auth-action"
									@click.prevent="authStore.login"
								>
									Đăng nhập
								</a>
							</div>
							<div
								v-else-if="authStore.profile"
								class="auth-list"
							>
								<i class="icon icon-user"> </i>
								<NuxtLink to="/account" class="auth-action">
									<span style="font-weight: 400">
										Xin chào</span
									>, {{ authStore.profile.displayName }}
								</NuxtLink>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="bottom-group">
			<div class="container">
				<div class="bottom-group-inner">
					<div class="logo">
						<NuxtLink to="/">
							<img src="/imgs/logo.png" alt="Lacdau.com" />
						</NuxtLink>
					</div>
					<div>
						<button
							class="nav-btn"
							@click="isActivemMenu = !isActivemMenu"
						>
							<i class="icon icon-nav"></i> Danh mục
						</button>

						<HeaderMenuList
							:categories="[]"
							:isActive="isActivemMenu"
							@closeMenu="isActivemMenu = false"
						/>
					</div>
					<form method="GET" class="header-form">
						<label class="form-label" for="search">
							<i class="icon icon-search"></i>
						</label>
						<input
							class="form-input"
							type="text"
							name="search"
							id="search"
							placeholder="Bạn cần tìm gì ?"
						/>
					</form>

					<div class="bottom-group-action-list">
						<div class="action-item">
							<div class="action-icon">
								<i class="icon icon-phone"></i>
							</div>
							<div class="action-detail">
								<p>Holine</p>
								<p class="highlight">0349.296.461</p>
							</div>
						</div>
						<div class="action-item">
							<div class="action-item">
								<div class="action-icon">
									<i class="icon icon-thunder"></i>
								</div>
								<div class="action-detail">
									<p>Tư vấn trực tiếp</p>
								</div>
							</div>
						</div>
					</div>

					<div
						class="header-cart"
						@mouseenter="showCart = true"
						@mouseleave="showCart = false"
					>
						<NuxtLink to="/cart">
							<button class="btn btn--cart">
								<i class="icon icon-cart">
									<ClientOnly>
										<span class="cart-quantity">{{
											cartStore.itemCount
										}}</span>
									</ClientOnly>
								</i>
								Giỏ hàng
							</button>
						</NuxtLink>

						<ClientOnly>
							<Transition name="cart">
								<TheCart
									v-if="showCart"
									:isCartEmpty="cartStore.isCartEmpty"
									:itemCount="cartStore.itemCount"
									:items="cartStore.items"
									:subTotalPrice="cartStore.subTotalPrice"
								/>
							</Transition>
						</ClientOnly>
					</div>
				</div>
			</div>
		</div>
	</header>
</template>

<style lang="scss">
@use '~/assets/scss/components/_header.scss';

.cart-enter-active {
	opacity: 1;
	transition: ease-in-out 200ms all;
}
.cart-leave-active {
	transition: ease-in-out 200ms all;
}

.cart-leave-to,
.cart-enter-from {
	translate: 0 30px;
	opacity: 0;
}
</style>
