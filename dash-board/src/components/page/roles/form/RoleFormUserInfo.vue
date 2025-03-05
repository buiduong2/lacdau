<script setup lang="ts">
import type { UserPermissionDetail } from '@/types/roles/resTypes'
import { formatDate } from '@/utils'
defineProps<{ user: UserPermissionDetail }>()

const fieldShow: (keyof UserPermissionDetail)[] = ['displayName', 'email', 'provider']
const label: string[] = ['Tên', 'Email', 'Nhà cung cấp']
</script>
<template>
  <Table>
    <TableBody>
      <TableRow>
        <TableCell class="w-40">Avatar: </TableCell>
        <TableCell><img :src="user.avatarUrl ?? '/placeholder.svg'" class="size-32" /></TableCell>
      </TableRow>
      <TableRow v-for="(field, index) in fieldShow" :key="index">
        <TableCell class="w-40">{{ label[index] }} </TableCell>
        <TableCell>
          <span v-if="user[field]">{{ user[field] }}</span>
          <span v-else class="text-destructive">Chưa có</span>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell class="w-40">Ngày tạo</TableCell>
        <TableCell>{{ formatDate(user.createdAt) }}</TableCell>
      </TableRow>
    </TableBody>
  </Table>
</template>

<style scoped></style>
