<script setup lang="ts">
import { Icon } from '@iconify/vue';
const salesPlatformLinks = [
	{
		href: '#Shopee',
		title: 'Shopee',
		icon: 'icon icon-shopee',
		isCustomIcon: true
	},
	{
		href: '#Lazada',
		title: 'Lazada',
		icon: 'icon icon-lazada',
		isCustomIcon: true
	},
	{
		href: '#Instagram',
		title: 'Instagram',
		icon: 'fa-instagram',
		isCustomIcon: false
	},
	{
		href: '#Tiktok',
		title: 'Tiktok',
		icon: 'fa6-brands:tiktok',
		isCustomIcon: false
	},
	{
		href: '#Youtube',
		title: 'Youtube',
		icon: 'fa-youtube',
		isCustomIcon: false
	},
	{
		href: '#Facebook',
		title: 'Facebook',
		icon: 'fa6-brands:square-facebook',
		isCustomIcon: false
	}
]
const isActivemMenu = ref(false)
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
							<div class="auth-list">
								<i class="icon icon-user"> </i>
								<a href="#" class="auth-action">Đăng ký</a>
								<span>/</span>
								<a href="#" class="auth-action">Đăng nhập</a>
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

					<div class="header-cart">
						<NuxtLink to="/cart">
							<button class="btn btn--cart">
								<i class="icon icon-cart">
									<span class="cart-quantity"></span>
								</i>
								Giỏ hàng
							</button>
						</NuxtLink>
					</div>
				</div>
			</div>
		</div>
	</header>
</template>

<style lang="scss">
@use '~/assets/scss/components/_header.scss';
</style>
