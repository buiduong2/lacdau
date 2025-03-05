<template>
	<select
		v-bind="props"
		v-model="model"
		@blur="onBlur"
		class="form-select"
		:class="{ error: error }"
	>
		<slot></slot>
	</select>
	<p class="form-message error" v-if="error">*{{ error }}</p>
</template>

<script setup lang="ts">
const props = defineProps<{
	name: string
}>()
const model = defineModel()

const { error, setValue, value, onBlur } = useField(props.name)
watch([model], () => {
	setValue(model.value)
})
</script>

<style scoped></style>
