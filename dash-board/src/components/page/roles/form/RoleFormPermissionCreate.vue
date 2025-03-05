<script setup lang="ts">
import type { Authority } from '@/types/auth/resTypes'
import { permissionsMeta } from './permissions'
defineProps<{ authorities: Authority[] }>()
</script>
<template>
  <AppFormSwitch
    v-if="authorities.length"
    v-for="authority in authorities"
    :name="'permission.' + authority"
    :key="authority"
    :label="permissionsMeta[authority].label"
    :icon="permissionsMeta[authority].icon"
    :desc="permissionsMeta[authority].desc"
  />
  <p v-else class="text-sm text-muted-foreground text-center italic">
    Có vẻ như không có quyền khả dụng
  </p>
</template>

<style scoped></style>
