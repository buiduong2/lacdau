<script setup lang="ts">
import { OrderStage } from '@/types/orders/resType';
import { toTypedSchema } from '@vee-validate/zod';
import { Form } from 'vee-validate';
import { z } from 'zod';
const props = defineProps<{ order: Awaited<ReturnType<typeof useOrder>> }>();
const router = useRouter();
const schema = [
  z.object({//OrderItem
    suppliedQuantity: z.boolean(), // Hãy kiểm tra số lượng sản phẩm
    quantityStatus: z.boolean(), // Hãy kiểm tra xem trạng thái của Order Item
    otherInfo: z.boolean()
  }),
  z.object({ // Order Address
    userInfo: z.boolean(),// Thông tin người dùng hợp lệ , tên , email ,phone: có thể liên lạc,
    addressInfo: z.boolean(), // Thông tin địa chỉ giao hàng hợp lệ có thể xử lý
    messageInfo: z.boolean(), // Đã đọc và hiểu các tin nhắn riêng
  }),
  z.object({
    paymentMethodInfo: z.boolean(), // Hiểu được về payment
    paymentSubTotalPriceInfo: z.boolean(), // Giá cả không có vấn đề gì
  }),

  z.object({
    message: z.string(),
  })
]

const stepIndex = ref(1);


const steps = [
  {
    step: 1,
    title: 'Sản phẩm trong giỏ hàng',
    description: 'Hãy kiểm tra giỏ hàng',
  },
  {
    step: 2,
    title: 'Địa chỉ giao hàng',
    description: 'Hãy kiểm tra thông tin đỉa chỉ giao hàng',
  },
  {
    step: 3,
    title: 'Thanh toán',
    description: 'Kiểm tra thông tin thanh toán',
  },
  {
    step: 4,
    title: 'Thành công',
    description: 'Tiến thành chuyển đổi trạng thái',
  },
]

const infoDescs: string[] = [
  'Thông tin giỏ hàng',
  'Thông tin địa chỉ giao hàng',
  'Thông tin Thanh toán được tính toán sơ lược',
  'Không có thông tin'
];

async function onSubmit(values: any) {
  const result = await props.order.updateProcess(OrderStage.CONFIRMATION, values.message);
  if (result) {
    router.replace({ name: '/orders/[id]/process/confirmation', params: { id: props.order.order.value.id } });
  }

}

</script>

<template>

  <div class="mt-5">
    <Form v-slot="{ meta, values, validate }" as="" keep-values
      :validation-schema="toTypedSchema(schema[stepIndex - 1])">
      <Stepper v-slot="{ isNextDisabled, isPrevDisabled, nextStep, prevStep }" v-model="stepIndex" class="block w-full">
        <form class="flex flex-col gap-6" @submit="(e) => {
          e.preventDefault()
          validate()

          if (stepIndex === steps.length && meta.valid) {
            onSubmit(values)
          }
        }">
          <AppFormGroup title="Tiến trình" desc="Tiến trình xử lý đơn hàng mới">
            <div class="flex w-full flex-start gap-2">
              <StepperItem v-for="step in steps" :key="step.step" v-slot="{ state }"
                class="relative flex w-full flex-col items-center justify-center" :step="step.step">
                <StepperSeparator v-if="step.step !== steps[steps.length - 1].step"
                  class="absolute left-[calc(50%+20px)] right-[calc(-50%+10px)] top-5 block h-0.5 shrink-0 rounded-full bg-muted group-data-[state=completed]:bg-primary" />

                <StepperTrigger as-child>
                  <Button :variant="state === 'completed' || state === 'active' ? 'default' : 'outline'" size="icon"
                    class="z-10 rounded-full shrink-0"
                    :class="[state === 'active' && 'ring-2 ring-ring ring-offset-2 ring-offset-background']"
                    :disabled="state !== 'completed' && !meta.valid">
                    <Icon icon="lucide:check" v-if="state === 'completed'" class="size-5" />
                    <Icon icon="lucide:circle" v-if="state === 'active'" />
                    <Icon icon="lucide:dot" v-if="state === 'inactive'" />
                  </Button>
                </StepperTrigger>

                <div class="mt-5 flex flex-col items-center text-center">
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
            </div>
          </AppFormGroup>

          <AppFormGroup title="Thông tin" :desc="infoDescs[stepIndex - 1]">
            <OrderProcessInfoOrderItem :order="order.order.value" v-if="stepIndex === 1" />
            <OrderProcessInfoOrderAddress :order="order.order.value" v-if="stepIndex === 2" />
            <OrderProcessInfoPayment :order="order.order.value" v-if="stepIndex === 3" />
          </AppFormGroup>

          <AppFormGroup title="Yêu cầu" desc="Hãy thực hiện các yêu cầu theo tiến trình">
            <div class="flex flex-col gap-4 mt-4">
              <OrderProcessPendingFormConfirmOrderItem v-if="stepIndex === 1" />
              <OrderProcessPendingFormConfirmAddress v-if="stepIndex === 2" />
              <OrderProcessPendingFormConfirmPayment v-if="stepIndex === 3" />
              <AppFormFieldTextArea label="Ghi chú tiến trình"
                desc="Hãy mô tả về đơn hàng trước khi kết thúc tiến trình hiện tại" v-if="stepIndex === 4"
                name="message" placeholder="Xác nhận đơn hàng không có vấn đề gì ..." />
            </div>
          </AppFormGroup>

          <div class="flex items-center justify-between">
            <Button :disabled="isPrevDisabled" variant="outline" size="sm" @click="prevStep()">
              Quay lại
            </Button>
            <div class="flex items-center gap-3">
              <Button v-if="steps.length !== stepIndex" :type="meta.valid ? 'button' : 'submit'"
                :disabled="isNextDisabled" size="sm" @click="meta.valid && nextStep()">
                Tiếp tục
              </Button>
              <Button v-if="steps.length === stepIndex" size="sm" type="submit">
                Submit
              </Button>
            </div>
          </div>
        </form>
      </Stepper>
    </Form>
  </div>
</template>


<style scoped></style>
