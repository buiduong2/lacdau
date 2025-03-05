<script setup lang="ts">
import type { EmployeeProfile } from '@/types/profile/resTypes';
import { AxiosError } from 'axios';
import { formatDate } from '@/utils';

definePage({
  meta: {
    breadcrumb: 'Thông tin cá nhân',
  },
})
const router = useRouter();
const auth = useAuthStore();

const profile = auth.profile!;
let empProfile: EmployeeProfile;
try {
  const res = await fetchEmployeeProfile();
  empProfile = res.data;
} catch (error) {
  if (error instanceof AxiosError && error.status === 403) {
    router.push('/profile/register');
  }
}



</script>
<template>
  <AppFormLayout title="Thông tin cá nhân" v-if="empProfile">
    <AppFormGroup desc="Thông tin chi tiết" title="Thông tin chi tiết">
      <Table>
        <TableHeader>
          <TableRow>
            <TableCell></TableCell>
            <TableCell colspan="1">
              <Avatar size="lg" class="ml-5">
                <AvatarImage :src="profile.avatarUrl || ''" :alt="profile.displayName" />
                <AvatarFallback class="rounded-lg">
                  {{ profile.displayName.trim()[0].toUpperCase() }}
                </AvatarFallback>
              </Avatar>
            </TableCell>
          </TableRow>
        </TableHeader>
        <TableBody>
          <TableRow>
            <TableHead class="w-44"> ID Employee: </TableHead>
            <TableCell> 1111</TableCell>
          </TableRow>
          <TableRow>
            <TableHead class="w-44"> Họ và tên: </TableHead>
            <TableCell> {{ empProfile.lastName }} {{ empProfile.firstName }}</TableCell>
          </TableRow>

          <TableRow>
            <TableHead class="w-44"> Giới tính: </TableHead>
            <TableCell>{{ empProfile.gender == 'MALE' ? 'Nam' : 'Nữ' }}</TableCell>
          </TableRow>

          <TableRow>
            <TableHead class="w-44"> Ngày tháng năm sinh </TableHead>
            <TableCell>{{ empProfile.dob }}</TableCell>
          </TableRow>

          <TableRow>
            <TableHead class="w-44"> Số điện thoại </TableHead>
            <TableCell>{{ empProfile.phone }}</TableCell>
          </TableRow>

          <TableRow>
            <TableHead class="w-44">Địa chỉ Email</TableHead>
            <TableCell>{{ empProfile.email }}</TableCell>
          </TableRow>


          <TableRow>
            <TableHead class="w-44">Ngày tạo</TableHead>
            <TableCell>{{ formatDate(empProfile.createdAt) }}</TableCell>
          </TableRow>

          <TableRow>
            <TableHead class="w-44">Ngày sửa đổi </TableHead>
            <TableCell>{{ formatDate(empProfile.updatedAt) }}</TableCell>
          </TableRow>



        </TableBody>
      </Table>
    </AppFormGroup>
    <AppFormGroup desc="Thông tin Đỉa chỉ" title="Thông tin địa chỉ">
      <Table>
        <TableBody>
          <TableRow>
            <TableHead class="w-44"> Tỉnh/ Thành phố </TableHead>
            <TableCell>{{ empProfile.address.province }}</TableCell>
          </TableRow>

          <TableRow>
            <TableHead class="w-44"> Giới tính: </TableHead>
            <TableCell>{{ empProfile.address.district }}</TableCell>
          </TableRow>

          <TableRow>
            <TableHead class="w-44">Thông tin chi tiết</TableHead>
            <TableCell>{{ empProfile.address.detail }}</TableCell>
          </TableRow>
        </TableBody>
      </Table>

    </AppFormGroup>

    <Button @click.prevent="router.push('/profile/edit')">Chỉnh sửa</Button>
  </AppFormLayout>
</template>
<style scoped></style>
