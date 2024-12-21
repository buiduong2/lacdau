<template>
	<div class="col-3">
		<label class="form-label" :for="name">Giới tính</label>
	</div>
	<div class="col-9">
		<div class="form-radio-group">
			<label
				class="form-radio-label"
				v-for="value in values"
				:key="value.value"
			>
				<input
					class="form-radio"
					type="radio"
					:name="name"
					:value="value.value"
					@input="onInput"
				/>
				{{ value.label }}
			</label>
		</div>
		<p class="form-message error" v-if="error">{{ error }}</p>
	</div>
</template>

<script setup lang="ts">
const props = defineProps<{
	name: string
	label: string
	values: { value: any; label: string }[]
}>()
const { error, onBlur, onInput } = useField(props.name)
</script>

<style scoped></style>
