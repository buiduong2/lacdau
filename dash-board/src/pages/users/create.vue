<script setup lang="ts">
import { fetchUserCheckEmail, fetchUserCheckUsername } from '@/apis/users'
import AppFormAsyncMessage from '@/components/page/app/form/AppFormAsyncMessage.vue'
import { addErrorFromServer } from '@/components/page/app/form/utils'
import { schema } from '@/components/page/users/form/schema'
import { toast } from '@/components/ui/toast'
import { toTypedSchema } from '@vee-validate/zod'
import { AxiosError } from 'axios'
import { useForm } from 'vee-validate'
definePage({
  meta: { breadcrumb: 'Tạo mới' },
})
const form = useForm({
  validationSchema: toTypedSchema(schema),
})
const onSubmit = form.handleSubmit(async (formValue) => {
  try {
    await fetchUserCreate(formValue)
    toast({ description: 'Thêm User thành công' })
    form.resetForm()
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.status === 400 && error.response?.data) {
        addErrorFromServer(form, error.response.data)
      }
    }
  }
})
</script>
<template>
  <div>
    <AppFormLayout title="Tạo người dùng mới">
      <AppFormGroup title="Người dùng" desc="Nhập thông tin chi tiết về người dùng">
        <form class="flex gap-4 flex-col" @submit.prevent="onSubmit">
          <AppFormFieldInput
            type="text"
            name="displayName"
            label="Tên người dùng"
            placeholder="Nguyễn Văn A"
          />

          <AppFormFieldInput
            type="email"
            name="email"
            label="Email"
            desc="Email sẽ dùng để các dịch vụ tài khoản và thông báo"
            placeholder="Email@gmail.com"
          >
            <AppFormAsyncMessage
              name="email"
              :form="form"
              :validator="fetchUserCheckEmail"
              error-message="Email đã tồn tại"
              success-message="Email chưa có ai sử dụng"
            />
          </AppFormFieldInput>
          <AppFormFieldInput
            type="text"
            name="username"
            desc="Tên tài khoản dùng để đăng nhập"
            label="Username"
            placeholder="buiduong1"
          >
            <AppFormAsyncMessage
              name="username"
              :form="form"
              :validator="fetchUserCheckUsername"
              error-message="Username đã tồn tại"
              success-message="Username chưa có ai sử dụng"
            />
          </AppFormFieldInput>
          <AppFormFieldInput type="password" name="password" label="Password" placeholder="****" />
          <AppFormFieldInput
            type="password"
            name="confirmPassword"
            label="Nhập lại mật khẩu"
            placeholder="****"
          />
          <Button>Xác nhận</Button>
        </form>
      </AppFormGroup>
    </AppFormLayout>
  </div>
</template>
<style scoped></style>
