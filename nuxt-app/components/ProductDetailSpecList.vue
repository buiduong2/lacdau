<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'
const props = defineProps<{ specifications: string[] }>()

const isOverFlow = props.specifications.length > 4
const isShow = ref(false)
</script>

<template>
	<div class="detail-item detail-body-spec">
		<h4 class="detail-item-title">Thông số sản phẩm</h4>

		<ul class="spec-list" :class="{ show: isShow }" data-show-id="spec-list">
			<li
				class="spec-item"
				v-for="(spec, index) in specifications"
				:key="index"
			>
				<Icon class="spec-item-icon" icon="fa6-solid:circle" />
				{{ spec }}
			</li>
		</ul>

		<div class="detail-item-action" v-if="isOverFlow">
			<button class="detail-item-action-btn" @click="isShow = !isShow">
				{{ isShow ? 'Thu gọn' : 'Xem thêm' }}
				<Icon
					class="detail-item-action-icon"
					:icon="
						isShow ? 'fa6-solid:up-long' : 'fa6-solid:right-long'
					"
				/>
			</button>
		</div>
	</div>
</template>

<style>
[data-show-id="spec-list"] > li:nth-child(n + 5) {
	display: none;
}

[data-show-id="spec-list"].show > li {
	display: block;
}
</style>
