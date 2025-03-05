<script setup lang="ts">
import AppFormDatePicker from '@/components/page/app/form/AppFormDatePicker.vue';
import { addErrorFromServer } from '@/components/page/app/form/utils';
import { schema } from '@/components/page/profile/form/registerSchema';
import { toast } from '@/components/ui/toast';
import { toTypedSchema } from '@vee-validate/zod';
import { AxiosError } from 'axios';
import { useForm } from 'vee-validate';

definePage({ meta: { breadcrumb: 'Đăng kí thông tin người dùng' } })

const router = useRouter();
const form = useForm({
  validationSchema: toTypedSchema(schema),
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
    await fetchEmployeeProfileCreate(body);
    toast({ description: "Đằng kí thông tin thành công" });
    router.push('/profile/info')
  } catch (error) {
    if (error instanceof AxiosError && error.status === 400) {

      addErrorFromServer(form, error.response?.data)
    }
    toast({ type: 'foreground', description: "Có lỗi xảy ra" })
  }
})

const provinceRes = await fetchProvinces();
const provinces: { label: string, value: string }[] = provinceRes.data.map(p => ({ label: p.name, value: String(p.id) }));

const districts = ref<{ label: string, value: string }[] | undefined>();
let provinceId: undefined | string = undefined;
async function fetchDistricts(): Promise<{ label: string, value: string }[]> {
  const res = await fetchProvince(provinceId!);
  return res.data.districts.map(d => ({ label: d.name, value: String(d.id) }));
}

watch([form.values], async () => {
  if (form.values.address?.provinceId != provinceId) {
    if (form.values.address?.provinceId) {
      provinceId = form.values.address.provinceId;
      districts.value = await fetchDistricts();
    } else {
      provinceId = undefined;
      districts.value = undefined;
    }
  }
})


</script>

<template>
  <AppFormLayout title="Đăng kí thông tin tài khoản">
    <form class="flex gap-4 flex-col" @submit.prevent="onSubmit">
      <AppFormGroup desc="Đăng kí thông tin cá nhân nội bộ trong cửa hàng" title="Đăng kí thông tin nhân viên">
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
        <AppFormFieldSelect name="address.provinceId" label="Tỉnh/Thành phố" placeholder="Chọn tỉnh thành phố"
          is-required :values="provinces" />

        <AppFormFieldSelect name="address.districtId" label="Quận/Huyện" placeholder="Chọn tỉnh Quận huyện" is-required
          v-if="districts" :values="districts" />

        <AppFormFieldTextArea name="address.detail" placeholder="Đường A. Quận B. TP.C" label="Thông tin chi tiết" />
      </AppFormGroup>

      <Button>Đăng kí</Button>
    </form>
  </AppFormLayout>
</template>


<style scoped></style>
