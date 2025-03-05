<script setup lang="ts">
import { schema } from '@/components/page/users/form/updateSchema'
import { toast } from '@/components/ui/toast'
import type { User } from '@/types/users/resTypes'
import { toTypedSchema } from '@vee-validate/zod'
import { AxiosError } from 'axios'
import { useForm } from 'vee-validate'
import { fetchUserCheckEmail } from '@/apis/users'
import { addErrorFromServer } from '@/components/page/app/form/utils'

definePage({
  meta: { breadcrumb: 'Chỉnh sửa' },
})

const route = useRoute('/users/[id]/')
const router = useRouter()
const userId = route.params.id
let user: User | null = null
try {
  const res = await fetchUser(userId)
  user = res.data
} catch (error) {
  if (error instanceof AxiosError) {
    if (error.status === 404) {
      router.push('/404')
    }
  }
}

const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: {
    displayName: user?.displayName,
    email: user?.userType === 'SYS' ? user.email : undefined,
  },
})

function asyncValidatorEmail(email: any): Promise<{ data: boolean }> {
  return user?.email === email ? Promise.resolve({ data: false }) : fetchUserCheckEmail(email)
}
const onSubmit = form.handleSubmit(async (formValue) => {
  try {
    await fetchUserEdit(userId, formValue)
    toast({ description: 'Thay đổi thông tin người dùng thành công' })
    form.setValues({})
  } catch (error) {
    if (error instanceof AxiosError && error.status === 400) {
      addErrorFromServer(form, error.response?.data)
    }
  }
})
</script>
<template>
  <AppFormLayout title="Chỉnh sửa người dùng" v-if="user">
    <form @submit.prevent="onSubmit" class="flex flex-col gap-6">
      <AppFormGroup
        title="Thông tin chi tiết về người dùng"
        :desc="
          user.userType === 'OAUTH'
            ? 'Vì là tài khoản của bên thứ 3 cung cấp. \nMỗi lần đăng nhập lại sẽ tự động đồng bộ hóa. Nên hệ thống không quản lý sự thay đổi'
            : 'Thay đổi thông tin về người dùng'
        "
      >
        <UserFormInfor :user="user">
          <template v-slot:displayName v-if="user.userType === 'SYS'">
            <AppFormFieldInput type="text" name="displayName" placeholder="Nhập tên người dùng" />
          </template>
          <template v-slot:email v-if="user.userType === 'SYS'">
            <AppFormFieldInput type="email" name="email" placeholder="Nhập Email ở đây">
              <AppFormAsyncMessage
                name="email"
                :form="form"
                :validator="asyncValidatorEmail"
                error-message="Email đã tồn tại"
                success-message="Bạn có thể sử dụng Email này"
              />
            </AppFormFieldInput>
          </template>
        </UserFormInfor>
      </AppFormGroup>
      <Button v-if="user.userType === 'SYS'">Xác nhận</Button>
    </form>
  </AppFormLayout>
</template>

<style scoped></style>
