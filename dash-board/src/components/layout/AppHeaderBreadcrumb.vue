<script setup lang="ts">
const route = useRoute()

const breadcrumbs = computed<BreadCrumb[]>(() => {
  const names: string[] = []
  const paths: string[] = []

  route.matched.forEach((r) => {
    const meta = r.meta.breadcrumb
    const path = r.path

    if (meta) {
      names.push(r.meta.breadcrumb)
      paths.push(path)
    }
  })

  if (paths.length > names.length) {
    paths.splice(0, paths.length - names.length)
  }

  return paths.map((_, index) => ({ text: names[index], url: paths[index] }))
})
</script>

<template>
  <Breadcrumb class="hidden md:flex">
    <BreadcrumbList>
      <BreadcrumbItem>
        <BreadcrumbLink as-child>
          <RouterLink to="/">Trang chủ</RouterLink>
        </BreadcrumbLink>
      </BreadcrumbItem>
      <template v-for="(breadcrumb, index) in breadcrumbs" :key="index">
        <BreadcrumbSeparator />
        <BreadcrumbItem>
          <BreadcrumbLink as-child>
            <RouterLink :to="breadcrumb.url">{{ breadcrumb.text }}</RouterLink>
          </BreadcrumbLink>
        </BreadcrumbItem>
      </template>
    </BreadcrumbList>
  </Breadcrumb>
</template>
