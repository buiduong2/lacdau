interface Options {
	defaultIndex?: number
	timeout: number
}

export const useFocusSlide = <S>(slides: S[], options: Options) => {
	const { defaultIndex = 0, timeout = 2000 } = options
	const currentIndex = ref<number>(defaultIndex)

	const activeSlide = computed<S>(() => slides[currentIndex.value])

	function nextSlide() {
		changeSlide(currentIndex.value + 1)
	}

	function prevSlide() {
		changeSlide(currentIndex.value - 1)
	}

	function changeSlide(index: number) {
		currentIndex.value = index
	}

	let timeoutId: NodeJS.Timeout
	function countDownChangeSlide(): void {
		timeoutId = setTimeout(() => {
			const nextIndex =
				currentIndex.value + 1 === slides.length
					? 0
					: currentIndex.value + 1
			changeSlide(nextIndex)
			countDownChangeSlide()
		}, timeout)
	}

	onMounted(() => countDownChangeSlide())

	onUnmounted(() => {
		clearTimeout(timeoutId)
	})

	watch(currentIndex, () => {
		clearTimeout(timeoutId)
		countDownChangeSlide()
	})

	return {
		currentIndex,
		activeSlide,
		nextSlide,
		prevSlide,
		changeSlide
	}
}
