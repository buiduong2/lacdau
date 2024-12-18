interface Config {
	wrapperEl: Ref<HTMLElement | null>
	totalItem: number
	visibleItemCount: number
}
type Options = {
	slideInterval?: number
	initialDelay?: number
	autoSlide?: boolean
}

export function useGallerySlide(config: Config, options?: Options) {
	const { wrapperEl, totalItem, visibleItemCount } = config
	const {
		slideInterval = 3000,
		initialDelay = 0,
		autoSlide = true
	} = options || {}
	let lastIndex = totalItem - visibleItemCount
	let slideStep = 100 / visibleItemCount

	const currentIndex = ref(0)
	const isAtStart = computed<boolean>(() => currentIndex.value === 0)
	const isAtEnd = computed<boolean>(() => currentIndex.value === lastIndex)

	const watchHandler = watch(currentIndex, () => {
		if (!wrapperEl.value) throw 'error Supplier not found Element'
		wrapperEl.value.style.translate = `-${currentIndex.value * slideStep}%`
	})

	let intervalId: NodeJS.Timeout
	let isForward = true
	function startCountdownChangeSlide() {
		if (!autoSlide) return
		intervalId = setInterval(() => {
			if (isForward) {
				if (!isAtEnd.value) {
					next()
				} else {
					prev()
					isForward = false
				}
			} else {
				if (!isAtStart.value) {
					prev()
				} else {
					isForward = true
					next()
				}
			}
		}, slideInterval)
	}

	function resetCountDown() {
		if (!autoSlide) return
		stopCountDown()
		startCountdownChangeSlide()
	}

	function stopCountDown() {
		clearInterval(intervalId)
	}

	function next() {
		if (currentIndex.value === lastIndex) return
		resetCountDown()
		currentIndex.value++
	}

	function prev() {
		if (currentIndex.value === 0) return
		resetCountDown()
		currentIndex.value--
	}

	onMounted(() => {
		if (!wrapperEl.value) throw new Error('Wrapper not bound')

		intervalId = setTimeout(() => {
			startCountdownChangeSlide()
		}, initialDelay)
	})

	onUnmounted(() => {
		clearTimeout(intervalId)
		stopCountDown()
		watchHandler.stop()
	})

	return {
		stopCountDown,
		resetCountDown,
		next,
		prev,
		isAtEnd,
		isAtStart
	}
}
