<template>
	<div :class="colLabel">
		<label class="form-label" :for="name">{{ label }}</label>
	</div>
	<div :class="colInput">
		<select class="form-select" :name="name" v-model="model">
			<slot></slot>
		</select>
		<p class="form-message error" v-if="error">{{ error }}</p>
	</div>
</template>

<script setup lang="ts">
const props = withDefaults(
	defineProps<{
		colLabel?: string
		colInput?: string
		label: string
		name: string
	}>(),
	{
		colInput: 'col-9',
		colLabel: 'col-3'
	}
)
const model = defineModel()

const { error, setValue, value } = useField(props.name)
watch([model], () => {
	setValue(model.value)
})
</script>

<style scoped></style>
