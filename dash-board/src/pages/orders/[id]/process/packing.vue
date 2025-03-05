<script setup lang="ts">
import { toast } from '@/components/ui/toast';
import { OrderStage, OrderType, PaymentStatus } from '@/types/orders/resType';
import { toTypedSchema } from '@vee-validate/zod';
import { Form } from 'vee-validate';
import { z } from 'zod';


const props = defineProps<{ order: Awaited<ReturnType<typeof useOrder>> }>();
const router = useRouter();

const stepIndex = ref(1);

const steps = [
  {
    step: 1,
    title: 'Đóng gói đơn hàng',
    description: 'Kiểm tra/ chuẩn bị đóng gói đơn hàng',
  },
  {
    step: 2,
    title: 'Chuẩn bị vận chuyển',
    description: 'Người vận chuyển đã đến nơi và chấp nhận đơn hàng. (Nếu có)',
  },
  {
    step: 3,
    title: 'Thành công',
    description: 'Tiến thành chuyển đổi trạng thái',
  },
]

const infoDescs: string[] = ['Thông tin đơn hàng', 'Thông tin vận chuyển', 'Thông tin vận chuyển'];
const isOrderTypeInStore = props.order.order.value.type === OrderType.IN_STORE;
const schema = [
  z.object({
    orderItemQualityInfo: z.boolean(),
    packingCompleted: z.boolean(),
    shippmentContacted: z.boolean(),
  }),
  z.object({
    shipmentCarrierArrived: z.boolean().default(isOrderTypeInStore),
    shipmentInfo: z.boolean().default(isOrderTypeInStore).refine(() => Boolean(props.order.order.value.shipment), "Hãy cập nhật thông tin vận chuyển"),
    shippingPrice: z.boolean().default(isOrderTypeInStore),
  }),
  z.object({
    message: z.string(),
  })
]

const openUpdateShipmentUpdateDialog = ref<boolean>(false);


async function onSubmit(message: string) {
  if (isOrderTypeInStore) {
    const result = await props.order.updateProcess(OrderStage.SUCCESSFUL, message);
    if (result) {
      router.replace({ name: '/orders/[id]/process/successful', params: { id: props.order.order.value.id } })
    }
  } else {
    const result = await props.order.updateProcess(OrderStage.SHIPPING, message);
    if (result) {
      router.replace({ name: '/orders/[id]/process/shipping', params: { id: props.order.order.value.id } })
    }
  }
}

async function onShipmentChange() {
  props.order.refresh();
  openUpdateShipmentUpdateDialog.value = false;

  toast({ description: "Cập nhật thông tin giao hàng thành công" });
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
            onSubmit(values.message)
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

          <AppFormGroup title="Thông tin" :desc="infoDescs[stepIndex]">
            <OrderProcessInfoOrderItem :order="order.order.value" v-if="stepIndex === 1" />
            <OrderProcessInfoShipment :order="order.order.value" v-if="stepIndex === 2 && !isOrderTypeInStore" />
          </AppFormGroup>

          <AppFormGroup title="Hành động" desc="Một số hành động có thể thực hiện trong tiến trình">
            <Dialog v-model:open="openUpdateShipmentUpdateDialog" v-if="!isOrderTypeInStore">
              <DialogTrigger as-child>
                <Button variant="outline">
                  Thêm/Chỉnh sửa thông tin vận chuyển
                </Button>
              </DialogTrigger>
              <OrderProcessShipmentUpdateDialogContent v-if="openUpdateShipmentUpdateDialog" :id="order.order.value.id"
                :shipment="order.order.value.shipment" @shipment-change="onShipmentChange" />
            </Dialog>
          </AppFormGroup>

          <AppFormGroup title="Yêu cầu" desc="Hãy thực hiện các yêu cầu theo tiến trình">
            <div class="flex flex-col gap-4 mt-4">
              <OrderProcessPackingFormConfirmPacking v-if="stepIndex === 1" />
              <OrderProcessPackingFormConfirmShipment v-if="stepIndex === 2" />
              <AppFormFieldTextArea label="Ghi chú về đơn hàng"
                desc="Hãy mô tả về đơn hàng trước khi kết thúc tiến trình hiện tại" v-if="stepIndex === steps.length"
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
