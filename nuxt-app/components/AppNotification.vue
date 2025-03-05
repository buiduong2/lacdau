<script setup lang="ts">
const props = withDefaults(
	defineProps<{
		msg?: string
		show?: boolean
		isSuccess?: boolean
	}>(),
	{
		isSuccess: true
	}
)

const message = ref(props.msg || '')
const state = ref(true)
const show = ref<boolean>(Boolean(props.show))
type Option = {
	liveTime?: number
	isSuccess?: boolean
}

async function showNotification(msg: string, option?: Option) {
	if (show.value) {
		return
	}

	const { liveTime = 1500, isSuccess = true } = option || {}
	message.value = msg
	show.value = true
	state.value = isSuccess
	setTimeout(() => {
		show.value = false
	}, liveTime)
}

provide('notification', { notification: showNotification })
</script>

<template>
	<Transition name="notification">
		<div class="notification" v-if="show">
			<div class="overlay"></div>
			<div class="notification-content">
				<div class="c-container">
					<div
						v-if="state"
						class="o-circle c-container__circle o-circle__sign--success"
					>
						<div class="o-circle__sign"></div>
					</div>

					<div
						v-else
						class="o-circle c-container__circle o-circle__sign--failure"
					>
						<div class="o-circle__sign"></div>
					</div>
				</div>

				<p class="notification-message">
					{{ message }}
				</p>
			</div>
		</div>
	</Transition>
	<slot></slot>
</template>

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

/*=======================
       C-Container
=========================*/
.c-container {
	max-width: 27rem;
	margin: 1rem auto 0;
	padding: 1rem;
}

/*=======================
       O-Circle
=========================*/

.o-circle {
	display: flex;
	width: 10.555rem;
	height: 10.555rem;
	justify-content: center;
	align-items: flex-start;
	border-radius: 50%;
	animation: circle-appearance 0.8s ease-in-out 1 forwards,
		set-overflow 0.1s 1.1s forwards;
}

.c-container__circle {
	margin: 20px auto 20px;
}

/*=======================
    C-Circle Sign
=========================*/

.o-circle__sign {
	position: relative;
	opacity: 0;
	background: #fff;
	animation-duration: 0.8s;
	animation-delay: 0.2s;
	animation-timing-function: ease-in-out;
	animation-iteration-count: 1;
	animation-fill-mode: forwards;
}

.o-circle__sign::before,
.o-circle__sign::after {
	content: '';
	position: absolute;
	background: inherit;
}

.o-circle__sign::after {
	left: 100%;
	top: 0%;
	width: 500%;
	height: 95%;
	transform: translateY(4%) rotate(0deg);
	border-radius: 0;
	opacity: 0;
	animation: set-shaddow 0s 1.13s ease-in-out forwards;
	z-index: -1;
}

/*=======================
      Sign Success
=========================*/

.o-circle__sign--success {
	background: rgb(56, 176, 131);
}

.o-circle__sign--success .o-circle__sign {
	width: 1rem;
	height: 6rem;
	border-radius: 50% 50% 50% 0% / 10%;
	transform: translateX(130%) translateY(35%) rotate(45deg) scale(0.11);
	animation-name: success-sign-appearance;
}

.o-circle__sign--success .o-circle__sign::before {
	bottom: -17%;
	width: 100%;
	height: 50%;
	transform: translateX(-130%) rotate(90deg);
	border-radius: 50% 50% 50% 50% / 20%;
}

/*--shadow--*/
.o-circle__sign--success .o-circle__sign::after {
	background: rgb(40, 128, 96);
}

/*=======================
      Sign Failure
=========================*/

.o-circle__sign--failure {
	background: rgb(236, 78, 75);
}

.o-circle__sign--failure .o-circle__sign {
	width: 1rem;
	height: 7rem;
	transform: translateY(25%) rotate(45deg) scale(0.1);
	border-radius: 50% 50% 50% 50% / 10%;
	animation-name: failure-sign-appearance;
}

.o-circle__sign--failure .o-circle__sign::before {
	top: 50%;
	width: 100%;
	height: 100%;
	transform: translateY(-50%) rotate(90deg);
	border-radius: inherit;
}

/*--shadow--*/
.o-circle__sign--failure .o-circle__sign::after {
	background: rgba(175, 57, 55, 0.8);
}

/*-----------------------
      @Keyframes
--------------------------*/

/*CIRCLE*/
@keyframes circle-appearance {
	0% {
		transform: scale(0);
	}

	50% {
		transform: scale(1.5);
	}

	60% {
		transform: scale(1);
	}

	100% {
		transform: scale(1);
	}
}

/*SIGN*/
@keyframes failure-sign-appearance {
	50% {
		opacity: 1;
		transform: translateY(25%) rotate(45deg) scale(1.7);
	}

	100% {
		opacity: 1;
		transform: translateY(25%) rotate(45deg) scale(1);
	}
}

@keyframes success-sign-appearance {
	50% {
		opacity: 1;
		transform: translateX(130%) translateY(35%) rotate(45deg) scale(1.7);
	}

	100% {
		opacity: 1;
		transform: translateX(130%) translateY(35%) rotate(45deg) scale(1);
	}
}

@keyframes set-shaddow {
	to {
		opacity: 1;
	}
}

@keyframes set-overflow {
	to {
		overflow: hidden;
	}
}

/*+++++++++++++++++++
    @Media Queries
+++++++++++++++++++++*/

@media screen and (min-width: 1300px) {
	HTML {
		font-size: 1.5625em;
	} /* 25 / 16 = 1.5625 */
}
</style>
