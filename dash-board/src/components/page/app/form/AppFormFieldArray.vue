<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { FormFieldArrayProps } from './type'
import { ErrorMessage, FieldArray, useField, useFieldError } from 'vee-validate'
import { FormField } from '@/components/ui/form'

const props = defineProps<FormFieldArrayProps>()
const { errorMessage } = useField(props.name)
</script>

<template>
  <div>
    <FieldArray v-slot="{ fields, push, remove }" :name="name">
      <div v-for="(field, index) in fields" :key="`${name}-${field.key}`">
        <FormField v-slot="{ componentField }" :name="`${name}[${index}]`">
          <FormItem>
            <FormLabel :class="cn(index !== 0 && 'sr-only')" v-if="label">
              {{ label }} <span v-if="isRequired" class="text-destructive">*</span>
            </FormLabel>
            <FormDescription :class="cn(index !== 0 && 'sr-only')" v-if="desc">
              {{ desc }}.
            </FormDescription>
            <div class="relative flex items-center">
              <slot v-bind="componentField">
                <FormControl> <Input type="text" v-bind="componentField" /> </FormControl>
              </slot>
              <button
                v-if="fields.length > 1"
                type="button"
                class="absolute top-0 py-2.5 pe-3 end-0 text-muted-foreground"
                @click="remove(index)"
              >
                <Icon icon="radix-icons:cross-1" class="w-3" />
              </button>
            </div>
            <FormMessage />
          </FormItem>
        </FormField>
      </div>

      <Button
        type="button"
        variant="outline"
        size="sm"
        class="text-xs w-20 mt-2"
        @click="push(prototypeItem || '')"
      >
        Thêm
      </Button>
      <br />
      <ErrorMessage :name="name" class="text-[0.8rem] font-medium text-destructive" />
    </FieldArray>
  </div>
</template>
