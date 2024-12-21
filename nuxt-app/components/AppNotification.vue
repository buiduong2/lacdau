<template>
	<Transition name="notification">
		<div class="notification" v-if="show">
			<div class="overlay"></div>
			<div class="notification-content">
				<AppCircleCheckIcon class="notification-icon" />
				<p class="notification-message">
					{{ message }}
				</p>
			</div>
		</div>
	</Transition>
	<slot></slot>
</template>

<script setup lang="ts">
const message = ref('')
const show = ref<boolean>(false)
type Option = {
	liveTime: number
}
async function showNotification(msg: string, option?: Option) {
	if (show.value) {
		return
	}

	const { liveTime = 1500 } = option || {}
	message.value = msg
	show.value = true
	setTimeout(() => {
		show.value = false
	}, liveTime)
}

provide('notification', { notification: showNotification })
</script>

<style scoped>
.notification-leave-active {
	transition: opacity ease-in-out 200ms;
}

.notification-leave-to {
	opacity: 0;
}

.notification-content {
	position: fixed;
	top: 50%;
	left: 50%;
	translate: -50% -50%;
	padding: 20px;
	background: #fff;
	border-radius: 12px;
	width: 500px;
	height: 300px;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: space-around;
	z-index: 110;
}

.notification-icon {
	transform: scale(1.5);
}

.notification-message {
	font-size: 2.4rem;
	line-height: 1.5;
	text-align: center;
	font-weight: 600;
	color: #333;
}

.overlay {
	position: fixed;
	inset: 0;
	background: rgba(0, 0, 0, 0.5);
	z-index: 100;
}
</style>
