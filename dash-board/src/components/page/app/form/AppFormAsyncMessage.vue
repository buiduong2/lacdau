<template>
  <div v-if="isValidating">
    <Icon icon="lucide:loader" class="animate-spin text-sm" />
  </div>
  <div v-if="error !== undefined">
    <p class="text-sm text-destructive" v-if="error">{{ errorMessage }}</p>
    <p class="text-sm text-green-400" v-else>{{ successMessage }}</p>
  </div>
</template>

<script setup lang="ts" generic="T extends FormContext<any>">
import type { Option } from '@/composables/useApiValidate'
import type { FormContext } from 'vee-validate'
const props = defineProps<
  Option<T> & {
    successMessage: string
    errorMessage: string
  }
>()
const { error, isValidating } = useApiValidate({ ...props })
</script>
