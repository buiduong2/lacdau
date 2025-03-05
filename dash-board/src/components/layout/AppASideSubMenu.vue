<template>
  <SidebarMenuSub>
    <SidebarMenuSubItem v-for="subItem in subItems" :key="subItem.title">
      <SidebarMenuSubButton as-child>
        <RouterLink :to="subItem.url" class="hover:bg-accent" active-class="bg-accent">
          <span>{{ subItem.title }}</span>
        </RouterLink>
      </SidebarMenuSubButton>
    </SidebarMenuSubItem>
  </SidebarMenuSub>
</template>

<script setup lang="ts">
defineProps<{ subItems: { title: string; url: string }[] }>()
</script>

<style scoped></style>
