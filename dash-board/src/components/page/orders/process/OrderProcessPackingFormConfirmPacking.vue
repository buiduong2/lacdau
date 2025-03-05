<template>
  <AppFormFieldCheckbox label="Hàng hóa" desc="Đã kiểm tra chất lượng hàng hóa và số lượng thành công"
    name="orderItemQualityInfo" is-required />

  <AppFormFieldCheckbox label="Đóng gói" desc="Đã đóng gói sản phẩm thành công" name="packingCompleted" is-required />

  <AppFormFieldCheckbox label="Liên lạc với đơn vị vận chuyển"
    desc="Đã làm việc với đơn vị vận chuyển thành công, đang chờ tài xế đến nơi" name="shippmentContacted"
    is-required />

</template>

<script setup lang="ts">

</script>

<style scoped></style>
