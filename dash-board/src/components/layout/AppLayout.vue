<script setup lang="ts"></script>
<template>
  <div class="flex min-h-screen w-full flex-col bg-muted/40">
    <AppAside />
    <div class="flex flex-col sm:gap-4 sm:py-4 sm:pl-14">
      <AppHeader />
      <main class="grid flex-1 items-start gap-4 p-4 sm:px-6 sm:py-0 md:gap-8">
        <slot />
      </main>
    </div>
  </div>
</template>
<style scoped></style>
