<script setup lang="ts">

const props = defineProps<{ provinceIdName: string, districtIdName: string, provinceIdValue?: string }>();


const provinceRes = await fetchProvinces();
const provinces: { label: string, value: string }[] = provinceRes.data.map(p => ({ label: p.name, value: String(p.id) }));

const districts = ref<{ label: string, value: string }[] | undefined>();
let provinceId: undefined | string = undefined;
async function fetchDistricts(): Promise<{ label: string, value: string }[]> {
  const res = await fetchProvince(provinceId!);
  return res.data.districts.map(d => ({ label: d.name, value: String(d.id) }));
}

watch([props], async () => {
  if (props.provinceIdValue != provinceId) {
    districts.value = undefined;
    if (props.provinceIdValue) {

      provinceId = props.provinceIdValue;
      districts.value = await fetchDistricts();
    } else {
      provinceId = undefined;
    }
  }
}, {
  immediate: true
})
</script>
<template>
  <AppFormFieldSelect :name="provinceIdName" label="Tỉnh/Thành phố" placeholder="Chọn tỉnh thành phố" is-required
    :values="provinces" />

  <AppFormFieldSelect :name="districtIdName" label="Quận/Huyện" placeholder="Chọn tỉnh Quận huyện" is-required
    v-if="districts" :values="districts" />


</template>


<style scoped></style>
