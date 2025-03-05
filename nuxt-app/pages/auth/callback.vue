<template>
	<AppNotification msg="Đăng nhập thành công .... Đang chuyển hướng" show />
</template>

<script setup lang="ts">
definePageMeta({ layout: 'empty' })

onMounted(() => {
	console.log('mouted')

	if (window) {
		setTimeout(() => {
			window.close()
		}, 2000)
	}
})
</script>

<style scoped></style>
