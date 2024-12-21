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
	const currentTranslate = computed<number>(
		() => -1 * (currentIndex.value * slideStep)
	)
	const random = ref<number>(Math.random())

	const watchHandler = watch([currentTranslate, random], () => {
		if (!wrapperEl.value) throw 'error Supplier not found Element'
		wrapperEl.value.style.translate = `${currentTranslate.value}%`
	})

	let startX: number = 0
	let translateX: number = 0

	const onDragStart = function (e: MouseEvent) {
		if (!wrapperEl.value) return
		startX = e.pageX
		wrapperEl.value.style.transition = 'none'
	}

	const onDrag = function (e: MouseEvent) {
		if (!wrapperEl.value || e.pageX === 0) return
		translateX = startX - e.pageX
		wrapperEl.value.style.translate = `calc(${currentTranslate.value}% - ${translateX}px)`
	}

	const onDragEnd = function (e: MouseEvent) {
		if (!wrapperEl.value) return

		const wrapperElWidth = wrapperEl.value.clientWidth
		const stepWidth = (slideStep / 100) * wrapperElWidth
		const rawStep = translateX / stepWidth
		const step = rawStep > 0 ? Math.ceil(rawStep) : Math.floor(rawStep)
		resetDragState()
		changeIndex(step)
	}

	function resetDragState() {
		if (!wrapperEl.value) return
		startX = 0
		wrapperEl.value.style.transition = ''
		translateX = 0
	}

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

	function changeIndex(step: number) {
		if (currentIndex.value + step <= 0) {
			currentIndex.value = 0
		} else if (currentIndex.value + step >= lastIndex) {
			currentIndex.value = lastIndex
		} else {
			currentIndex.value = currentIndex.value + step
		}
		random.value = Math.random()
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
		onDrag,
		onDragStart,
		onDragEnd,
		isAtEnd,
		isAtStart
	}
}
