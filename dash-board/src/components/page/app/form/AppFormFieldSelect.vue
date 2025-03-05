<template>
  <AppFormField :desc="desc" :is-required="isRequired" :label="label" :name="name">
    <template #body="componentField">
      <Select v-bind="componentField">
        <FormControl>
          <SelectTrigger>
            <SelectValue :placeholder="placeholder" />
          </SelectTrigger>
        </FormControl>
        <SelectContent>
          <SelectGroup>
            <SelectItem :value="value.value" v-for="value in values" :key="value.value">
              {{ value.label }}<span v-if="isRequired" class="text-destructive">*</span>
            </SelectItem>
          </SelectGroup>
        </SelectContent>
      </Select>
    </template>
  </AppFormField>
</template>

<script setup lang="ts">
import AppFormField from './AppFormField.vue';
import type { FormFieldSelectProps } from './type';

defineProps<FormFieldSelectProps>()
</script>

<style scoped></style>
