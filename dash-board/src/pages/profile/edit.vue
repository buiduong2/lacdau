<script setup lang="ts">
import AppFormDatePicker from '@/components/page/app/form/AppFormDatePicker.vue';
import { addErrorFromServer } from '@/components/page/app/form/utils';
import { schema } from '@/components/page/profile/form/registerSchema';
import { toast } from '@/components/ui/toast';
import type { EmployeeProfile } from '@/types/profile/resTypes';
import { toTypedSchema } from '@vee-validate/zod';
import { AxiosError } from 'axios';
import { useForm } from 'vee-validate';

definePage({ meta: { breadcrumb: 'Chỉnh sửa thông tin cá nhân' } })

const router = useRouter();
let profile: EmployeeProfile | undefined;

try {

  const res = await fetchEmployeeProfile()
  profile = res.data;
} catch (error) {
  profile = undefined;
  router.push('/403');
}

const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: {
    firstName: profile!.firstName,
    lastName: profile!.lastName,
    email: profile!.email,
    phone: profile!.phone,
    gender: profile!.gender,
    dob: new Date(profile?.dob || 0),
    address: {
      detail: profile?.address.detail,
      provinceId: String(profile?.address.provinceId),
      districtId: String(profile?.address.districtId),
    }
  }
})

const onSubmit = form.handleSubmit(async (values) => {
  try {
    const body = {
      ...values,
      address: {
        detail: values.address.detail,
        provinceId: Number(values.address.provinceId),
        districtId: Number(values.address.districtId),
      }
    }
    await fetchEmployeeProfileEdit(body);
    toast({ description: "Đằng kí thông tin thành công" });
    router.push('/profile/info')
  } catch (error) {
    if (error instanceof AxiosError && error.status === 400) {

      addErrorFromServer(form, error.response?.data)
    }
    toast({ type: 'foreground', description: "Có lỗi xảy ra" })
  }
})


</script>

<template>
  <AppFormLayout title="Chỉnh sửa thông tin cá nhân">
    <form class="flex gap-4 flex-col" @submit.prevent="onSubmit">
      <AppFormGroup desc="Chỉnh sửa thông tin cá nhân nội bộ trong cửa hàng"
        title="Chỉnh sửa thông tin cá nhân nhân viên">
        <AppFormFieldInput name="firstName" desc="Tên sử dụng trong nội bộ cửa hàng" label="Tên riêng" placeholder="A"
          is-required type="text" />
        <AppFormFieldInput name="lastName" desc="Tên sử dụng trong nội bộ cửa hàng" label="Họ" placeholder="Nguyễn Văn"
          is-required type="text" />
        <AppFormFieldInput name="email" label="Email" placeholder="email@gmail.com" type="email" />
        <AppFormFieldInput name="phone" label="Số điện thoại" placeholder="01234567890" type="text" />

        <AppFormFieldRadio name="gender" is-required label="Giới tính"
          :options="[{ label: 'Nam', value: 'MALE' }, { label: 'Nữ', value: 'FEMALE' }]" />

        <AppFormDatePicker name="dob" label="Ngày tháng năm sinh" is-required />

      </AppFormGroup>

      <AppFormGroup desc="Thông tin địa chỉ của nhân viên" title="Đỉa chỉ">
        <AppFormAddressDistrict province-id-name="address.provinceId" district-id-name="address.districtId"
          :province-id-value="form.values.address?.provinceId" />
        <AppFormFieldTextArea name="address.detail" placeholder="Đường A. Quận B. TP.C" label="Thông tin chi tiết" />
      </AppFormGroup>

      <Button>Đăng kí</Button>
    </form>
  </AppFormLayout>
</template>


<style scoped></style>
