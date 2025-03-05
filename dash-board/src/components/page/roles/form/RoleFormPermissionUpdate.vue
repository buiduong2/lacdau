<script setup lang="ts">
import type { PermissionDetail } from '@/types/roles/resTypes'
import { formatDate } from '@/utils'
import AppFormSwitch from '../../app/form/AppFormSwitch.vue'
import { permissionsMeta } from './permissions'
defineProps<{
  permissions: PermissionDetail[]
}>()
</script>

<template>
  <AppFormSwitch
    v-if="permissions.length"
    :name="'permission.' + permission.permission"
    v-for="permission in permissions"
    :key="permission.id"
    :icon="permissionsMeta[permission.permission].icon"
    :label="permissionsMeta[permission.permission].label"
    :desc="permissionsMeta[permission.permission].desc"
  >
    <FormDescription class="text-destructive">
      Tạo mới:
      <RouterLink
        v-if="permission.createdBy"
        class="underline"
        :to="{ name: '/roles/[id]', params: { id: permission.createdBy.id } }"
      >
        {{ permission.createdBy.displayName }}
      </RouterLink>
      - {{ formatDate(permission.createdAt) }}
    </FormDescription>
    <FormDescription class="text-destructive">
      Chỉnh sửa :
      <RouterLink
        v-if="permission.createdBy"
        class="underline"
        :to="{ name: '/roles/[id]', params: { id: permission.createdBy.id } }"
      >
        {{ permission.createdBy.displayName }}
      </RouterLink>
      - {{ formatDate(permission.updatedAt) }}
    </FormDescription>
  </AppFormSwitch>
  <p v-else class="text-sm text-muted-foreground text-center italic">
    Có vẻ như đây là người dùng thường
  </p>
</template>

<style scoped></style>
