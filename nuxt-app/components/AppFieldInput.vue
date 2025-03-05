<template>
	<div :class="colLabel || 'col-3'">
		<label
			class="form-label"
			:class="{ 'label-required': required }"
			:for="name"
			>{{ label }}</label
		>
	</div>
	<div :class="colInput || 'col-9'">
		<input
			class="form-input"
			:class="{ error: error }"
			:type="type"
			:id="name"
			:name="name"
			:placeholder="placeholder"
			@input="onInput"
			@blur="onBlur"
            :value="value"
		/>
		<p class="form-message error" v-if="error">{{ error }}</p>
	</div>
</template>

<script setup lang="ts">
const props = defineProps<{
	name: string
	type: string
	placeholder?: string
	label: string
	colLabel?: string
	colInput?: string
	required?: boolean
}>()
const { error, onBlur, onInput,value } = useField(props.name)
</script>

<style scoped></style>
