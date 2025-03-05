<template>
  <AppFormFieldCheckbox label="Đơn vị vận chuyển" desc="Người vận chuyển đã dến nơi và đồng ý với việc giao hàng"
    name="shipmentCarrierArrived" is-required />

  <AppFormFieldCheckbox label="Thêm thông tin vận chuyển" desc="Đã thêm thông tin vận chuyển" name="shipmentInfo"
    is-required />

  <AppFormFieldCheckbox label="Thanh toán phí vận chuyển" desc="Đã thanh toán phí vận chuyển thành công"
    name="shippingPrice" is-required />

</template>

<script setup lang="ts">

</script>

<style scoped></style>
