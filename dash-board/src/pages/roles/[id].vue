<script setup lang="ts">
import { schema } from '@/components/page/roles/form/updateSchema'
import { toast } from '@/components/ui/toast'
import { Authority } from '@/types/auth/resTypes'
import type { UserPermissionDetail } from '@/types/roles/resTypes'
import { toTypedSchema } from '@vee-validate/zod'
import { AxiosError } from 'axios'
import { useForm } from 'vee-validate'
definePage({
  meta: { breadcrumb: 'Chỉnh sửa' },
})

const route = useRoute('/roles/[id]')
const router = useRouter()
const userId = route.params.id
let user: UserPermissionDetail | null = null
let addPermissions: Authority[] = []
try {
  const { data } = await fetchUserPermission(userId)
  user = data
  const userpermissions = user.permissions.map((p) => p.permission)
  addPermissions = Object.values(Authority).filter((a) => !userpermissions.includes(a))
} catch (error) {
  if (error instanceof AxiosError) {
    if (error.status === 404) {
      router.push('/404')
    }
  }
}

const store = useRoleStore()
const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: {
    permission: Object.fromEntries(
      user?.permissions.map((p) => [p.permission, !Boolean(p.revokedAt)]) || [],
    ),
  },
})

const onSubmit = form.handleSubmit(async (formValue) => {
  const permissions = Object.entries(formValue.permission)
    .filter(([key, value]) => value)
    .map(([key]) => key)

  await fetchUserpermissionUpdate(userId, { permissions: permissions as Authority[] })
  toast({ description: 'Chỉnh sửa thành công' })
  store.setDirty()
})
</script>
<template>
  <AppFormLayout :title="'Cấp quyền cho ' + user.displayName" v-if="user">
    <form class="flex gap-4 flex-col" @submit.prevent="onSubmit">
      <AppFormGroup title="Người dùng" desc="Chọn người dùng sẽ được cấp quyền">
        <div>
          <h4
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 mb-4"
          >
            Thông tin người dùng:
          </h4>
          <RoleFormUserInfo :user="user" />
        </div>
      </AppFormGroup>

      <AppFormGroup title="Chỉnh sửa quyền" desc="Hủy đi một quyền có sẵn">
        <RoleFormPermissionUpdate :permissions="user.permissions" />
      </AppFormGroup>

      <AppFormGroup title="Cấp quyền" desc="Cấp các quyền mới cho người dùng">
        <RoleFormPermissionCreate :authorities="addPermissions" />
      </AppFormGroup>

      <Button type="submit">Xác nhận</Button>
    </form>
  </AppFormLayout>
</template>

<style scoped></style>
