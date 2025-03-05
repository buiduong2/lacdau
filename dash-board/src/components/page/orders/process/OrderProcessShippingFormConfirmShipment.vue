<template>
  <AppFormFieldCheckbox label="Thông tin người nhận" desc="Đã kiểm tra thông tin khách hàng" name="orderAddressInfo"
    is-required />
  <AppFormFieldCheckbox label="Thông tin giao hàng" desc="Đã kiểm tra thông tin giao hàng" name="shipmentInfo"
    is-required />
  <AppFormFieldCheckbox label="Thông báo thành công" desc="Đơn vị giao hàng đã thông báo giao hàng thành công"
    name="shipmentSuccessful" is-required />
</template>

<script setup lang="ts">

</script>

<style scoped></style>
