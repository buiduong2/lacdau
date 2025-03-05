<script setup lang="ts">
const props = defineProps<{
	name: string
	placeholder?: string
}>()
const { error, onBlur, onInput } = useField(props.name)
</script>
<template>
	<textarea
		class="form-input"
		:class="{ error: error }"
		:id="name"
		:name="name"
		:placeholder="placeholder"
		@input="onInput"
		@blur="onBlur"
	></textarea>
	<p class="form-message error" v-if="error">*{{ error }}</p>
</template>

<style scoped>
</style>
