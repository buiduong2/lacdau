<script setup lang="ts">
import type { FormGroupToggleProps } from './type'

const props = defineProps<FormGroupToggleProps>()
const open = ref<boolean>(!!props.defaultOpen)
</script>
<template>
  <AppFormGroup :desc="desc" :title="title" class="relative">
    <Button v-if="!open" variant="outline" @click.stop="(open = true)" type="button"> Thêm </Button>
    <Button
      v-else="!open"
      class="absolute right-6 top-6"
      size="icon"
      variant="outline"
      @click.stop="(open = false)"
    >
      <Icon icon="lucide:x" />
    </Button>
    <slot v-if="open"></slot>
  </AppFormGroup>
</template>
