<script setup lang="ts">
const authStore = useAuthStore()
</script>

<template>
	<AppBreadcrumb
		:breadcrumbs="[
			{ href: 'login', id: 'random', name: 'Đăng nhập tài khoản' }
		]"
	/>
	<section class="section-login">
		<div class="container">
			<div class="row">
				<div class="col-6">
					<div class="form">
						<h3 class="section-title">
							Bạn chưa đăng nhập. Hãy đằng nhập
						</h3>

						<div class="row content-center items-center gx-3 gy-3">
							<div class="col-3"></div>
							<div class="col-9">
								<div class="form-action">
									<button
										class="form-btn"
										@click="authStore.login"
									>
										Mời bạn bấm vào đây để đăng nhập
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-6">
					<div class="section-detail">
						<h3 class="section-title">Bạn chưa là thành viên ?</h3>
						<p class="section-para">
							Đăng ký là thành viên để hưởng nhiều lợi ích và đặt
							mua hàng dễ dàng hơn.
						</p>

						<div class="section-action">
							<button
								class="section-link"
								@click="authStore.login()"
							>
								Đăng ký tài khoản
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</template>

<style scoped lang="scss">
@use '@/assets/scss/pages/login.scss';
</style>
