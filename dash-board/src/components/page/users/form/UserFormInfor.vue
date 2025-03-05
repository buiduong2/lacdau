<template>
  <Table>
    <TableHeader>
      <TableRow>
        <TableCell></TableCell>
        <TableCell colspan="1">
          <Avatar size="lg" class="ml-5">
            <AvatarImage :src="user.avatarUrl || ''" :alt="user.displayName" />
            <AvatarFallback class="rounded-lg">
              {{ user.displayName.trim()[0].toUpperCase() }}
            </AvatarFallback>
          </Avatar>
        </TableCell>
      </TableRow>
    </TableHeader>
    <TableBody>
      <TableRow>
        <TableHead class="w-44"> ID: </TableHead>
        <TableCell> {{ user.id }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead class="w-44"> Tài khoản: </TableHead>
        <TableCell> {{ user.username }}</TableCell>
      </TableRow>

      <TableRow>
        <TableHead> Tên hiển thị: </TableHead>
        <TableCell>
          <slot name="displayName" :data="user.displayName">
            {{ user.displayName }}
          </slot>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableHead> Email: </TableHead>
        <TableCell>
          <slot name="email">
            {{ user.email || 'Chưa có' }}
          </slot>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableHead> Người dùng: </TableHead>
        <TableCell>
          <Badge :variant="user.moderate ? 'default' : 'outline'" class="font-medium text-center">
            {{ user.moderate ? 'Admin' : 'Người dùng' }}
          </Badge>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableHead> Nhà cung cấp: </TableHead>
        <TableCell>
          <Badge :variant="getUserTypeVariant(user)" class="font-medium text-center">
            {{ getUserTypeLabel(user) }}
          </Badge>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableHead> Ngày tạo: </TableHead>
        <TableCell>
          {{ formatDate(user.createdAt) }}
        </TableCell>
      </TableRow>

      <TableRow>
        <TableHead> Ngày sửa đổi: </TableHead>
        <TableCell>
          {{ formatDate(user.updatedAt) }}
        </TableCell>
      </TableRow>
    </TableBody>
  </Table>
</template>

<script setup lang="ts">
import type { User } from '@/types/users/resTypes'
import { getUserTypeLabel, getUserTypeVariant } from '../table/column'
import { formatDate } from '@/utils'
defineProps<{ user: User }>()
</script>

<style scoped></style>
