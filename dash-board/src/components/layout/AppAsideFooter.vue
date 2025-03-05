<script setup lang="ts">
import { storeToRefs } from 'pinia'
import AppAsideUserInfo from './AppAsideUserInfo.vue'
const store = useAuthStore()
const { logout, getProfile, changePassword, getPermissions } = store

const { profile } = storeToRefs(useAuthStore())
</script>
<template>
  <SidebarFooter v-if="profile">
    <SidebarMenu>
      <SidebarMenuItem>
        <DropdownMenu>
          <DropdownMenuTrigger as-child>
            <SidebarMenuButton size="lg"
              class="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground">
              <AppAsideUserInfo :profile="profile" />
              <Icon icon="lucide:chevrons-down-up" class="ml-auto size-4" />
            </SidebarMenuButton>
          </DropdownMenuTrigger>
          <DropdownMenuContent class="w-[--radix-dropdown-menu-trigger-width] min-w-56 rounded-lg" side="bottom"
            align="end" :side-offset="4">
            <DropdownMenuLabel class="p-0 font-normal">
              <div class="flex items-center gap-2 px-1 py-1.5 text-left text-sm">
                <AppAsideUserInfo :profile="profile" />
              </div>
            </DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuGroup>
              <DropdownMenuItem @click="getProfile">
                <Icon icon="lucide:user-pen" />
                Tài khoản
              </DropdownMenuItem>
              <DropdownMenuItem @click="$router.push('/profile/info')">
                <Icon icon="lucide:user-pen" />
                Thông tin cá nhân
              </DropdownMenuItem>
              <DropdownMenuItem @click="getPermissions">
                <Icon icon="lucide:scale" />
                Quyền hạn
              </DropdownMenuItem>
              <DropdownMenuItem @click="changePassword">
                <Icon icon="lucide:fingerprint" />
                Đổi mật khẩu
              </DropdownMenuItem>
            </DropdownMenuGroup>
            <DropdownMenuSeparator />
            <DropdownMenuItem @click="logout">
              <Icon icon="lucide:log-out" />
              Log out
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </SidebarMenuItem>
    </SidebarMenu>
  </SidebarFooter>
  <SidebarRail />
</template>

<style scoped></style>
