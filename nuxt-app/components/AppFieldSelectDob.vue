<template>
	<div class="col-3">
		<label class="form-label" for="phone">Ngày Sinh</label>
	</div>
	<div class="col-9">
		<div class="form-select-group">
			<div class="row">
				<div class="col-3">
					<select
						class="form-select"
						name="day"
						@change="
							onSelect({
								key: 'day',
								target: $event.target as HTMLSelectElement
							})
						"
					>
						<option value="-1">-Ngày-</option>
						<option :value="day" v-for="day in days" :key="day">
							{{ day }}
						</option>
					</select>
				</div>

				<div class="col-3">
					<select
						class="form-select"
						name="month"
						@change="
							onSelect({
								key: 'month',
								target: $event.target as HTMLSelectElement
							})
						"
					>
						<option value="-1">-Tháng-</option>
						<option
							:value="month"
							v-for="month in months"
							:key="month"
						>
							{{ month }}
						</option>
					</select>
				</div>
				<div class="col-3">
					<select
						class="form-select"
						name="year"
						@change="
							onSelect({
								key: 'year',
								target: $event.target as HTMLSelectElement
							})
						"
					>
						<option value="-1">-Năm-</option>
						<option :value="year" v-for="year in years" :key="year">
							{{ year }}
						</option>
					</select>
				</div>
			</div>
		</div>
		<p class="form-message error" v-if="error">{{ error }}</p>
	</div>
</template>

<script setup lang="ts">
type SelectedEntry = {
	key: 'month' | 'year' | 'day'
	target: HTMLSelectElement
}
const currentYear = new Date().getFullYear()
const days: number[] = new Array(32).fill(null).map((v, index) => index + 1)
const months: number[] = new Array(12).fill(null).map((v, index) => index + 1)
const years: number[] = new Array(100)
	.fill(null)
	.map((v, index) => currentYear - index)

const { error, setValue, value } = useField('dob')

function onSelect(entry: SelectedEntry) {
	let key: string = entry.key
	let newEntryVal: number | undefined =
		Number(entry.target.value) === -1
			? undefined
			: Number(entry.target.value)
	if (!newEntryVal) {
		if (!value.value) {
			return
		}
		if (Object.keys(value.value).length === 1 && key in value.value) {
			setValue(undefined)
		} else {
			const newVal = { ...value.value }
			delete newVal[key]
			setValue(newVal)
		}
	} else {
		if (!value.value) {
			console.log({ [key]: newEntryVal })
			setValue({ [key]: newEntryVal })
		} else {
			setValue({ ...value.value, [key]: newEntryVal })
		}
	}
}
</script>

<style scoped></style>
