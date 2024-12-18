<script setup lang="ts">
const links = [
  { to: '/', icon: 'lucide:home', tooltip: 'Dashboard' },
  { to: '/order', icon: 'lucide:shopping-cart', tooltip: 'Orders' },
  { to: '/products', icon: 'lucide:package', tooltip: 'Products' },
  { to: '/categories', icon: 'lucide:cat', tooltip: 'Categories' },
  { to: '/brands', icon: 'lucide:air-vent', tooltip: 'Brands' },
  { to: '/attributes', icon: 'lucide:tags', tooltip: 'Attributes' },
  { to: '/attribute-values', icon: 'lucide:tag', tooltip: 'Attribute Values' },
  { to: '/relate-groups', icon: 'lucide:group', tooltip: 'Attribute Values' },
  { to: '/customer', icon: 'lucide:users', tooltip: 'Customers' },
  { to: '/analytics', icon: 'lucide:line-chart', tooltip: 'Analytics' },
]
</script>
<template>
  <TooltipProvider>
    <aside class="fixed inset-y-0 left-0 z-10 hidden w-14 flex-col border-r bg-background sm:flex">
      <nav class="flex flex-col items-center gap-4 px-2 py-4">
        <RouterLink
          :to="`/products`"
          class="group flex h-9 w-9 shrink-0 items-center justify-center gap-2 rounded-full bg-primary text-lg font-semibold text-primary-foreground md:h-8 md:w-8 md:text-base"
        >
          <Icon icon="lucide:package-2" class="h-4 w-4 transition-all group-hover:scale-110" />
          <span class="sr-only">Acme Inc</span>
        </RouterLink>

        <Tooltip v-for="link in links" :key="link.to">
          <TooltipTrigger as-child>
            <RouterLink
              :to="link.to"
              active-class="bg-accent text-accent-foreground"
              class="flex h-9 w-9 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:text-foreground md:h-8 md:w-8"
            >
              <Icon :icon="link.icon" class="h-5 w-5" />
              <span class="sr-only">{{ link.tooltip }}</span>
            </RouterLink>
          </TooltipTrigger>
          <TooltipContent side="right"> {{ link.tooltip }} </TooltipContent>
        </Tooltip>
      </nav>
      <nav class="mt-auto flex flex-col items-center gap-4 px-2 py-4">
        <Tooltip>
          <TooltipTrigger as-child>
            <a
              href="#"
              class="flex h-9 w-9 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:text-foreground md:h-8 md:w-8"
            >
              <Icon icon="lucide:settings" class="h-5 w-5" />
              <span class="sr-only">Settings</span>
            </a>
          </TooltipTrigger>
          <TooltipContent side="right"> Settings </TooltipContent>
        </Tooltip>
      </nav>
    </aside>
  </TooltipProvider>
</template>

<style scoped></style>
