export async function useProvinces(opt?: {
	initialValues?: {
		provinceId?: number
		districtId?: number
	}
}) {
	const { initialValues } = opt || {}
	const currentProvinceId = ref<string | number | undefined>(
		initialValues?.provinceId
	)
	const currentDistrictId = ref<string | number | undefined>(
		initialValues?.districtId
	)

	const provinceId = computed<number | undefined>({
		get: () =>
			currentProvinceId.value
				? Number(currentProvinceId.value)
				: undefined,
		set: newValue => {
			currentDistrictId.value = undefined
			currentProvinceId.value = newValue
		}
	})

	const districtId = computed<number | undefined>({
		get: () =>
			currentDistrictId.value
				? Number(currentDistrictId.value)
				: undefined,
		set: newValue => {
			currentDistrictId.value = newValue
		}
	})

	const provincesRes = useFetch('/api/provinces')
	const provinceRes = provinceId.value
		? useFetch(`/api/provinces/${provinceId.value}`)
		: Promise.resolve({ data: { value: { district: [] } } })
	const [{ data, error }, { data: province }] = await Promise.all([
		provincesRes,
		provinceRes
	])
	if (error.value) {
		throw createError({
			statusCode: 500,
			message: "Can't not fetch /api/provinces"
		})
	}
	const provinces = data
	const districts = ref<Province['districts']>(
		'districts' in province.value! ? province.value.districts : []
	)

	watch([provinceId], async () => {
		if (provinceId.value === undefined) {
			districts.value = []
		} else {
			districts.value = (
				await $fetch(`/api/provinces/${provinceId.value}`)
			).districts
		}
	})

	return {
		districtId,
		provinceId,
		provinces,
		districts
	}
}
