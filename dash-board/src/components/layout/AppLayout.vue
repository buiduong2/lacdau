<script setup lang="ts"></script>
<template>
  <div class="bg-muted/40">
    <SidebarProvider>
      <AppAside />
      <div class="flex flex-col sm:gap-4 sm:py-4 w-full">
        <AppHeader />
        <main class="grid flex-1 items-start gap-4 p-4 sm:px-6 sm:py-0 md:gap-8">
          <slot />
        </main>
      </div>
    </SidebarProvider>
  </div>
</template>
<style scoped></style>
