<script setup lang="ts">
import { schema } from '@/components/page/roles/form/createSchema'
import { toast } from '@/components/ui/toast'
import { Authority } from '@/types/auth/resTypes'
import type { UserPermissionDetail } from '@/types/roles/resTypes'
import { toTypedSchema } from '@vee-validate/zod'
import { AxiosError } from 'axios'
import { useForm } from 'vee-validate'
definePage({
  meta: { breadcrumb: 'Tạo mới' },
})

const store = useRoleStore()

const form = useForm({
  validationSchema: toTypedSchema(schema),
})

const gettingUser = ref<boolean>(false)

const user = ref<UserPermissionDetail>()

const userPermissions = computed<Authority[]>(
  () => user?.value?.permissions.map((p) => p.permission) || [],
)

const addPermissions = computed<Authority[]>(() =>
  Object.values(Authority).filter((a) => !userPermissions.value.includes(a)),
)

const fetchUser = debounce(1000, async (userId?: number) => {
  if (userId) {
    try {
      const { data } = await fetchUserPermission(userId)
      user.value = data
      form.setFieldValue(
        'permission',
        Object.fromEntries(data.permissions.map((p) => [p.permission, !Boolean(p.revokedAt)])),
      )
    } catch (error) {
      user.value = undefined
      if (error instanceof AxiosError) {
        if (error.status === 404) {
          form.setFieldError('userId', 'Không tìm thấy User')
        }
      }
    }
  } else {
    user.value = undefined
  }
  gettingUser.value = false
})

const onSubmit = form.handleSubmit(async (formValue) => {
  const userId = formValue.userId
  const permissions = Object.entries(formValue.permission)
    .filter(([key, value]) => value)
    .map(([key]) => key)

  await fetchUserpermissionUpdate(userId, { permissions: permissions as Authority[] })
  toast({ description: 'Chỉnh sửa thành công' })
  store.setDirty()
  form.resetForm()
})

watch(form.values, ({ userId }) => {
  if (userId !== user.value?.id) {
    gettingUser.value = true
  }
  if (userId != user.value?.id) {
    fetchUser(userId)
  }
})
</script>

<template>
  <AppFormLayout title="Cấp quyền cho User mới">
    <form class="flex gap-4 flex-col" @submit.prevent="onSubmit">
      <AppFormGroup title="Người dùng" desc="Chọn người dùng sẽ được cấp quyền">
        <AppFormFieldInput
          name="userId"
          isRequired
          label="Id của người dùng"
          type="number"
          placeholder="123"
        />
        <div v-if="gettingUser"><Icon icon="lucide:loader" class="animate-spin" /></div>
        <div v-if="user">
          <h4
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 mb-4"
          >
            Thông tin người dùng:
          </h4>
          <RoleFormUserInfo :user="user" />
        </div>
      </AppFormGroup>

      <AppFormGroup title="Chỉnh sửa quyền" desc="Hủy đi một quyền có sẵn" v-if="user">
        <RoleFormPermissionUpdate :permissions="user.permissions" />
      </AppFormGroup>

      <AppFormGroup title="Cấp quyền" desc="Cấp các quyền mới cho người dùng" v-if="user">
        <RoleFormPermissionCreate :authorities="addPermissions" />
      </AppFormGroup>

      <Button type="submit">Xác nhận</Button>
    </form>
  </AppFormLayout>
</template>

<style scoped></style>
