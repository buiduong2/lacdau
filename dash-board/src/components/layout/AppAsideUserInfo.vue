<template>
  <Avatar class="h-8 w-8 rounded-lg">
    <AvatarImage :src="profile.avatarUrl || ''" :alt="profile.displayName" />
    <AvatarFallback class="rounded-lg">
      {{ profile.displayName.trim()[0].toUpperCase() }}
    </AvatarFallback>
  </Avatar>
  <div class="grid flex-1 text-left text-sm leading-tight">
    <span class="truncate font-semibold">{{ profile.displayName }}</span>
    <span class="truncate text-xs">{{ profile.email || 'Chưa có Email' }}</span>
  </div>
</template>

<script setup lang="ts">
import type { Profile } from '@/types/auth/resTypes'
defineProps<{ profile: Profile }>()
</script>

<style scoped></style>
