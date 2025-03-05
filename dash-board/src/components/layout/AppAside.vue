<script setup lang="ts">
import { Authority } from '@/types/auth/resTypes'
import AppAsideFooter from './AppAsideFooter.vue'

const authorities: Authority[] = useAuthStore().authorities
function hasAuthority(authorities: Authority[], authority: Authority): boolean {
  return authorities.includes(authority)
}

const groups = [
  {
    title: 'Quản lý bán hàng',
    navs: [
      {
        title: 'Quản lý kho hàng',
        url: '/products',
        icon: 'lucide:boxes',
        isActive: false,
        show: hasAuthority(authorities, Authority.PRODUCT_MANAGE),
        items: [
          { title: 'Sản phẩm', url: '/products' },
          { title: 'Nhóm Thuộc tính', url: '/attributes' },
          { title: 'Thuộc tính con', url: '/attribute-values' },
          { title: 'Thương hiệu', url: '/brands' },
          { title: 'Danh mục', url: '/categories' },
          { title: 'Nhóm sản phẩm', url: '/relate-groups' },
        ],
      },
      {
        title: 'Đơn hàng',
        url: '/orders',
        icon: 'lucide:shopping-cart',
        isActive: false,
        show: hasAuthority(authorities, Authority.ORDER_MANAGE),
      },
      {
        title: 'Khách hàng',
        url: '/customers',
        icon: 'lucide:circle-user-round',
        isActive: false,
        show: hasAuthority(authorities, Authority.CUSTOMER_MANAGE),
      },

      {
        title: 'Thống kê',
        url: '/analytics',
        icon: 'lucide:chart-no-axes-combined',
        isActive: false,
        show: hasAuthority(authorities, Authority.REPORT_VIEW),
      },
    ],
  },
  {
    title: 'Quản lý Trang web',
    navs: [],
    show: hasAuthority(authorities, Authority.REPORT_VIEW),
  },
  {
    title: 'Quản lý nội bộ',
    navs: [
      {
        title: 'Tài khoản & bảo mật',
        url: '/users',
        icon: 'lucide:user-round-cog',
        isActive: false,
        show: hasAuthority(authorities, Authority.USER_MANAGE),
        items: [
          {
            title: 'tài khoản',
            url: '/users',
          },
          {
            title: 'Vai trò người dùng',
            url: '/roles',
          },
        ],
      },
    ],
  },
].map((g) => ({
  ...g,
  navs: g.navs.filter((nav) => nav.show == undefined || nav.show),
}))
</script>
<template>
  <Sidebar collapsible="icon">
    <SidebarHeader>
      <SidebarMenu>
        <SidebarMenuButton>
          <Icon icon="lucide:home" />
          <span class="text-lg font-semibold">LẮC ĐẦU</span>
        </SidebarMenuButton>
      </SidebarMenu>
    </SidebarHeader>
    <SidebarContent>
      <SidebarGroup v-for="(group, index) in groups" :key="index">
        <SidebarGroupLabel>{{ group.title }} </SidebarGroupLabel>
        <SidebarGroupContent>
          <SidebarMenu>
            <Collapsible v-for="item in group.navs" :key="item.title" as-child :default-open="item.isActive"
              class="group/collapsible">
              <template v-if="item.items">
                <SidebarMenuItem>
                  <CollapsibleTrigger as-child>
                    <AppAsideMenuButton :item="item" />
                  </CollapsibleTrigger>

                  <CollapsibleContent>
                    <AppASideSubMenu :sub-items="item.items" />
                  </CollapsibleContent>
                </SidebarMenuItem>
              </template>

              <template v-else>
                <SidebarMenuItem>
                  <RouterLink :to="item.url">
                    <AppAsideMenuButton :item="item"></AppAsideMenuButton>
                  </RouterLink>
                </SidebarMenuItem>
              </template>
            </Collapsible>
          </SidebarMenu>
        </SidebarGroupContent>
      </SidebarGroup>
    </SidebarContent>
    <AppAsideFooter />
  </Sidebar>
</template>

<style scoped></style>
