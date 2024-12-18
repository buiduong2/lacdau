<script setup lang="ts">
defineProps<{ breadcrumbs: Breadcrumb[] }>()
</script>
<template>
	<section class="section-breadcrumb">
		<div class="container">
			<ul class="breadcrumb-list">
				<li class="breadcrumb-item">
					<NuxtLink
						class="breadcrumb-link"
						to="/"
						data-test="root-link"
						>TRANG CHỦ</NuxtLink
					>
				</li>
				<li
					v-for="bc in breadcrumbs"
					:key="bc.id"
					class="breadcrumb-item"
				>
					<NuxtLink class="breadcrumb-link" :to="bc.href" data-test="links">
						{{ bc.name }}
					</NuxtLink>
				</li>
			</ul>
		</div>
	</section>
</template>
<style scoped lang="scss">
@use '@/assets/scss/components/breadcrumb.scss';
</style>
