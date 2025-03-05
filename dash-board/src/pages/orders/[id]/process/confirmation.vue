<script setup lang="ts">
import { toast } from '@/components/ui/toast';
import { OrderStage } from '@/types/orders/resType';
import { toTypedSchema } from '@vee-validate/zod';
import { Form } from 'vee-validate';
import { z } from 'zod';

const props = defineProps<{ order: Awaited<ReturnType<typeof useOrder>> }>();
const router = useRouter();

const schema = [
  z.object({
    orderItemInfo: z.boolean(),
    orderItemQuantity: z.boolean(),
    orderItemSupplyInfo: z.boolean(),
  }),
  z.object({
    userInfo: z.boolean(),
    addressInfo: z.boolean(),
    messageInfo: z.boolean(),
  }),
  z.object({
    paymentSubTotalPriceInfo: z.boolean(),
  }),

  z.object({
    message: z.string(),
  })
]

const openDialogAddress = ref(false);
const openDialogOrderItem = ref(false);

const stepIndex = ref(1);
const infoDescs: string[] = [
  'Thông tin giỏ hàng',
  'Thông tin địa chỉ giao hàng',
  'Thông tin Thanh toán được tính toán sơ lược',
  'Không có thông tin'
];

const steps = [
  {
    step: 1,
    title: 'Sản phẩm trong giỏ hàng',
    description: 'Xác nhận với khách hoặc cập nhật nếu cần thiết',
  },
  {
    step: 2,
    title: 'Địa chỉ giao hàng',
    description: 'Xác nhận với khách hoặc cập nhật nếu cần thiết',
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


async function onSubmit(values: any) {
  const result = await props.order.updateProcess(OrderStage.PAYMENT, values.message);
  if (result) {
    router.replace({ name: '/orders/[id]/process/payment', params: { id: props.order.order.value.id } });
  }

}

function onOrderChange() {
  props.order.refresh()
  openDialogAddress.value = false;
  toast({ description: "Cập nhật đơn hàng thành công" });
}

function onOrderAddressChange() {
  props.order.refresh();
  openDialogAddress.value = false;
  toast({ description: "Cập nhật địa chỉ thành công" });
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

          <AppFormGroup title="Hành động" desc="Một số hành động có thể thực hiện trong tiến trình">
            <Dialog v-model:open="openDialogAddress">
              <DialogTrigger as-child>
                <Button variant="outline">
                  Chỉnh sửa thông tin giao hàng
                </Button>
              </DialogTrigger>
              <OrderProcessAddressUpdateDialogContent v-if="openDialogAddress" :id="order.order.value.id"
                :address="order.order.value.orderAddress" @address-change="onOrderAddressChange" />
            </Dialog>

            <Dialog v-model:open="openDialogOrderItem">
              <DialogTrigger as-child>
                <Button variant="outline">
                  Chỉnh sửa giỏ hàng
                </Button>
              </DialogTrigger>
              <OrderProcessOrderItemUpdateDialogContent v-if="openDialogOrderItem" :id="order.order.value.id"
                :order-items="order.order.value.orderItems" @order-items-change="onOrderChange" />
            </Dialog>
          </AppFormGroup>

          <AppFormGroup title="Yêu cầu" desc="Hãy thực hiện các yêu cầu theo tiến trình">
            <div class="flex flex-col gap-4 mt-4">
              <OrderProcessConfirmFormConfirmOrderItem v-if="stepIndex === 1" />
              <OrderProcessConfirmFormConfirmAddress v-if="stepIndex === 2" />
              <OrderProcessConfirmFormConfirmPayment v-if="stepIndex === 3" />
              <AppFormFieldTextArea label="Ghi chú về đơn hàng"
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

    <div id="dialog-container"></div>
  </div>
</template>


<style scoped></style>
