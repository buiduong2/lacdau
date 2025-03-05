<script setup lang="ts">
import { addErrorFromServer } from '@/components/page/app/form/utils'
import { schema } from '@/components/page/users/form/changePasswordSchema'
import UserFormInfor from '@/components/page/users/form/UserFormInfor.vue'
import { toast } from '@/components/ui/toast'
import type { User } from '@/types/users/resTypes'
import { toTypedSchema } from '@vee-validate/zod'
import { AxiosError } from 'axios'
import { useForm } from 'vee-validate'

definePage({
  meta: { breadcrumb: 'Đổi mật khẩu' },
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
})

const onSubmit = form.handleSubmit(async (formValue) => {
  try {
    await fetchUserChangePassword(userId, formValue)
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
  <AppFormLayout title="Đổi mật khẩu người dùng" v-if="user">
    <AppFormGroup title="Thông tin" desc="Thông tin chi tiết về người dùng">
      <UserFormInfor :user="user" />
    </AppFormGroup>
    <form @submit.prevent="onSubmit" class="flex flex-col gap-6" v-if="user.userType === 'SYS'">
      <AppFormGroup title="Đổi mật khẩu" desc="Đổi mật khẩu cho người dùng">
        <AppFormFieldInput
          type="password"
          name="password"
          label="Mật khẩu mới"
          is-required
          placeholder="*****"
        />
        <AppFormFieldInput
          type="password"
          name="confirmPassword"
          label="Nhập lại mật khẩu mới"
          is-required
          placeholder="*****"
        />
      </AppFormGroup>
      <Button>Xác nhận</Button>
    </form>
    <AppFormGroup title="Có lỗi" desc="Có lỗi đã xảy ra" v-else>
      <div class="text-center">
        <p>Có vẻ như bạn đang cố gắng thay đổi mật khẩu của một tài khoản của bên thứ 3</p>
        <p>Chúng ta không quản lý mật khẩu của họ nên ko thể đổi</p>
      </div>
    </AppFormGroup>
  </AppFormLayout>
</template>

<style scoped></style>
