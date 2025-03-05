<script setup lang="ts"></script>
<template>
  <header
    class="sticky top-0 z-30 flex h-14 items-center gap-4 border-b bg-background px-4 sm:static sm:h-auto sm:border-0 sm:bg-transparent sm:px-6"
  >
    <SidebarTrigger />
    <AppHeaderBreadcrumb />
    <div class="relative ml-auto flex-1 md:grow-0">
      <Icon icon="lucide:search" class="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
      <Input
        type="search"
        placeholder="Search..."
        class="w-full rounded-lg bg-background pl-8 md:w-[200px] lg:w-[320px]"
      />
    </div>
  </header>
</template>

<style scoped></style>
