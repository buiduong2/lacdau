import { useToast } from '@/components/ui/toast'
import type { OrderAddressUpdate } from '@/types/orders/reqType'
import type { Order, OrderStage } from '@/types/orders/resType'
import { AxiosError } from 'axios'

export type OrderComposable = Awaited<ReturnType<typeof useOrder>>

export async function useOrder(id: number | string) {
  const toast = useToast()

  const order = ref<Order>(await fetchInit())

  async function refresh() {
    order.value = await fetchInit()
  }

  async function updateProcess(stage: OrderStage, message: string): Promise<boolean> {
    try {
      const res = await fetchOrderProcess(id, stage, message)
      order.value.stage = res.data.stage
      toast.toast({ description: 'Cập nhật thành công' })
      return true
    } catch (error) {
      if (error instanceof AxiosError) {
        if (error.status === 400 && error.response?.data) {
          if (error.response.data.errors[0].field === 'nextStage') {
            toast.toast({
              variant: 'destructive',
              description: 'Cập nhật thất bại: ' + error.response.data.errors[0].message,
            })
            return false
          }
        }
      }

      toast.toast({ variant: 'destructive', description: 'Có lỗi xảy ra' })
    }
    return false
  }

  async function fetchInit(): Promise<Order> {
    return (await fetchOrder(id)).data
  }

  return {
    order,
    updateProcess,
    refresh,
  }
}
