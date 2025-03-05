<template>
  <SidebarMenuButton
    :tooltip="item.title"
    class="hover:bg-accent"
    :class="$route.path === item.url && !item.items ? 'bg-accent' : ''"
  >
    <Icon :icon="item.icon" />
    <span class="w-full">
      {{ item.title }}
      <SidebarMenuAction v-if="item.items">
        <Icon
          icon="lucide:chevron-right"
          class="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90"
        />
      </SidebarMenuAction>
    </span>
  </SidebarMenuButton>
</template>

<script setup lang="ts">
defineProps<{ item: { title: string; items?: any[]; icon: string; url: string } }>()
</script>

<style scoped></style>
