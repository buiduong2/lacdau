<script setup lang="ts">
import { OrderStage, type Order } from '@/types/orders/resType';
import { getStepDesc, getStepNumber } from './processSteps';

const props = defineProps<{ order: Order }>();

const steps = computed(() => getStepDesc(props.order.type, props.order.stage));
const step = computed(() => getStepNumber(steps.value, props.order.stage));
const cancelStages: OrderStage[] = [OrderStage.FAILURE, OrderStage.CANCELED];
const isCanceling = computed(() => cancelStages.includes(props.order.stage));
</script>
<template>
  <AppFormGroup title="Trạng thái tiến trình"
    desc="Tùy thuộc vào trạng thái hiện tại mà ta sẽ có các dòng sự kiên khác nhau">
    <Stepper orientation="vertical" class="mx-auto flex w-full max-w-md flex-col justify-start gap-10"
      :model-value="step">
      <StepperItem v-for="step in steps" :key="step.step" v-slot="{ state }"
        class="relative flex w-full items-start gap-6" :step="step.step">
        <StepperSeparator v-if="step.step !== steps[steps.length - 1].step"
          class="absolute left-[18px] top-[38px] block h-[105%] w-0.5 shrink-0 rounded-full bg-muted group-data-[state=completed]:bg-primary" />


        <Button :variant="state === 'completed' || state === 'active' ?
          isCanceling ? 'destructive' : 'default'
          : 'outline'" size="icon" class="z-10 rounded-full shrink-0 cursor-auto"
          :class="[state === 'active' && 'ring-2 ring-ring ring-offset-2 ring-offset-background']">
          <Icon :icon="step.icon" class="size-10" />
        </Button>


        <div class="flex flex-col gap-1">
          <StepperTitle :class="[state === 'active' && 'text-primary']"
            class="text-sm font-semibold transition lg:text-base">
            {{ step.title }}
          </StepperTitle>
          <StepperDescription :class="[state === 'active' && 'text-primary']"
            class="sr-only text-xs text-muted-foreground transition md:not-sr-only lg:text-sm">
            {{ step.description }}
          </StepperDescription>
        </div>
      </StepperItem>
    </Stepper>
  </AppFormGroup>
</template>


<style scoped></style>
