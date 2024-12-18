<!-- Chứa Datable Component của chúng ta -->
<script setup lang="ts">
defineProps<{
  row: any
}>()

defineEmits<{
  (e: 'clone', id: any): void
  (e: 'edit', id: any): void
  (e: 'remove', id: any): void
}>()
</script>

<template>
  <DropdownMenu>
    <DropdownMenuTrigger as-child>
      <Button variant="ghost" class="w-8 h-8 p-0">
        <span class="sr-only">Open menu</span>
        <Icon icon="lucide:more-horizontal" class="w-4 h-4" />
      </Button>
    </DropdownMenuTrigger>
    <DropdownMenuContent align="end">
      <DropdownMenuItem class="cursor-pointer" @click="$emit('edit', row.id)">
        <Icon icon="lucide:edit" /> Chỉnh sửa
      </DropdownMenuItem>
      <DropdownMenuItem class="cursor-pointer" @click="$emit('clone', row.id)">
        <Icon icon="lucide:copy" />Tạo bản sao
      </DropdownMenuItem>
      <DropdownMenuSeparator />
      <DropdownMenuItem class="cursor-pointer text-red-500" @click="$emit('remove', row.id)">
        <Icon icon="lucide:trash" />Xóa</DropdownMenuItem
      >
    </DropdownMenuContent>
  </DropdownMenu>
</template>
