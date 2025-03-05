<template>
  <AppFormFieldCheckbox label="Thông tin đơn hàng" desc="Đã xác nhận thông tin đơn hàng tiến hành hủy"
    name="orderItemInfo" is-required />
  <AppFormFieldCheckbox label="Số lượng mặt hàng"
    desc="Đã nhận được đủ số lượng mặt hàng (TH giao hàng trả về cửa hàng)" name="orderItemQuantity" is-required />
  <AppFormFieldCheckbox label="Tình trạng mặt hàng hàng" desc="Chất lượng mặt hàng không bị hư hại gì"
    name="orderItemQuality" is-required />
</template>

<script setup lang="ts">

</script>

<style scoped></style>
