import { OrderStage, OrderType } from '@/types/orders/resType'
export interface OrderProcessStep {
  step: number
  title: string
  stage: OrderStage
  icon: string
  description: string
}
// OrderStage.PENDING,
// OrderStage.COFIRMATION,
// OrderStage.PAYMENT,
// OrderStage.PACKING,
// OrderStage.SHIPPING,
// OrderStage.DELIVERED,
// OrderStage.SUCCESSFUL,
// OrderStage.COMPLETED,

// OrderStage.PENDING,
// OrderStage.COFIRMATION,
// OrderStage.PAYMENT,
// OrderStage.PACKING,
// OrderStage.SUCCESSFUL,
// OrderStage.COMPLETED,

const onlineSteps: OrderProcessStep[] = [
  {
    step: 1,
    stage: OrderStage.PENDING,
    icon: 'lucide:loader',
    title: 'Đợi xử lý',
    description: 'Đơn hàng vừa mới được đặt hãy xử lý',
  },
  {
    step: 2,
    stage: OrderStage.CONFIRMATION,
    icon: 'lucide:clipboard-list',
    title: 'Xác nhận',
    description: 'Xem lại thông tin đơn hàng trước khi bắt đầu tiến trình',
  },
  {
    step: 3,
    title: 'Thanh toán',
    stage: OrderStage.PAYMENT,
    icon: 'lucide:banknote',
    description: 'Hãy làm việc với khách hàng để xử lý thanh toán',
  },

  {
    step: 4,
    title: 'Đóng gói',
    stage: OrderStage.PACKING,
    icon: 'lucide:package-check',
    description: 'Kiểm tra lại SL và chất lượng sản phẩm',
  },

  {
    step: 5,
    title: 'Vận chuyển',
    stage: OrderStage.SHIPPING,
    icon: 'lucide:truck',
    description: 'Quá trình vận chuyển của sản phẩm',
  },
  {
    step: 6,
    title: 'Giao hàng thành công',
    stage: OrderStage.DELIVERED,
    icon: 'lucide:map-pin-check',
    description: 'Quá trình vận chuyển của sản phẩm',
  },

  {
    step: 7,
    title: 'Thành công',
    stage: OrderStage.SUCCESSFUL,
    icon: 'lucide:flag',
    description: 'Quá trình vận chuyển của sản phẩm',
  },
  {
    step: 8,
    title: 'Hoàn tất',
    stage: OrderStage.COMPLETED,
    icon: 'lucide:circle-check',
    description: 'Quá trình vận chuyển của sản phẩm',
  },
]
const inStoreSteps: OrderProcessStep[] = [
  {
    step: 1,
    stage: OrderStage.PENDING,
    icon: 'lucide:loader',
    title: 'Đợi xử lý',
    description: 'Đơn hàng vừa mới được đặt hãy xử lý',
  },
  {
    step: 2,
    stage: OrderStage.CONFIRMATION,
    icon: 'lucide:clipboard-list',
    title: 'Xác nhận',
    description: 'Xem lại thông tin đơn hàng trước khi bắt đầu tiến trình',
  },
  {
    step: 3,
    title: 'Thanh toán',
    stage: OrderStage.PAYMENT,
    icon: 'lucide:banknote',
    description: 'Hãy làm việc với khách hàng để xử lý thanh toán',
  },

  {
    step: 4,
    title: 'Đóng gói',
    stage: OrderStage.PACKING,
    icon: 'lucide:package-check',
    description: 'Kiểm tra lại SL và chất lượng sản phẩm',
  },
  {
    step: 5,
    title: 'Thành công',
    stage: OrderStage.SUCCESSFUL,
    icon: 'lucide:flag',
    description: 'Quá trình vận chuyển của sản phẩm',
  },
  {
    step: 6,
    title: 'Hoàn tất',
    stage: OrderStage.COMPLETED,
    icon: 'lucide:circle-check',
    description: 'Quá trình vận chuyển của sản phẩm',
  },
]

const cancelSteps: OrderProcessStep[] = [
  {
    step: 1,
    title: 'Thất bại',
    stage: OrderStage.FAILURE,
    icon: 'lucide:triangle-alert',
    description: 'Đơn hàng trong tiến trình hủy',
  },
  {
    step: 2,
    title: 'Đã hủy',
    stage: OrderStage.CANCELED,
    icon: 'lucide:circle-x',
    description: 'Hủy đơn hàng thành công',
  },
]

export function getStepDesc(type: OrderType, currentStage: OrderStage): OrderProcessStep[] {
  const cancelStage = [OrderStage.FAILURE, OrderStage.CANCELED]
  if (cancelStage.includes(currentStage)) {
    return cancelSteps
  }
  if (type == OrderType.IN_STORE) {
    return inStoreSteps
  }
  return onlineSteps
}

export function getStepNumber(steps: OrderProcessStep[], currentStage: OrderStage): number {
  return steps.find((step) => currentStage === step.stage)?.step || -1
}
