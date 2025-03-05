<script setup lang="ts">
const props = defineProps<{
	name: string
	type: string
	placeholder?: string
}>()
const { error, onBlur, onInput } = useField(props.name)
</script>
<template>
	<input
		class="form-input"
		:class="{ error: error }"
		:type="type"
		:id="name"
		:name="name"
		:placeholder="placeholder"
		@input="onInput"
		@blur="onBlur"
	/>
	<p class="form-message error" v-if="error">*{{ error }}</p>
</template>

<style scoped>
</style>
