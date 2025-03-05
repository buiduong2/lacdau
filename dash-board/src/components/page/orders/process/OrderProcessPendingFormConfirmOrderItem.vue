<template>
  <AppFormFieldCheckbox label="Số lượng sản phẩm được cung cấp"
    desc="Đã kiểm tra số lượng sản phẩm được cung cấp cho đơn hàng. Có thể Số lượng sản phẩm sẽ không tương ứng với số lượng yêu cầu "
    name="suppliedQuantity" is-required />
  <AppFormFieldCheckbox label="Trạng từng mặt hàng"
    desc="Đã kiểm tra trạng thái của mặt hàng. Có thể sẽ có mặt hàng không thể cung cấp" name="quantityStatus"
    is-required />

  <AppFormFieldCheckbox label="Thông tin khác" desc="Đã kiểm tra thông tin khác." name="otherInfo" is-required />
</template>

<script setup lang="ts">

</script>

<style scoped></style>
