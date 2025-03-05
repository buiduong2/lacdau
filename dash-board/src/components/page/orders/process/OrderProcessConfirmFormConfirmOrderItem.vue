<template>
  <AppFormFieldCheckbox label="Thông tin sản phẩm" desc="Đã xác nhận với khách hàng các sản phẩm cần mua"
    name="orderItemInfo" is-required />
  <AppFormFieldCheckbox label="Số lượng mặt hàng" desc="Đã xác nhận với khách hàng số lượng mặt hàng. Có thể thếm bớt"
    name="orderItemQuantity" is-required />

  <AppFormFieldCheckbox label="Số lượng mặt hàng thực tế"
    desc="Đã xác nhận khách đồng ý nếu có sự sai lệch về số lượng mong muốn" name="orderItemSupplyInfo" is-required />


</template>

<script setup lang="ts">

</script>

<style scoped></style>
