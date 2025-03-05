<script setup lang="ts">
import { FormField } from '@/components/ui/form';
import { OrderItemStatus, type Order } from '@/types/orders/resType';
import type { Product } from '@/types/products/resTypes';
import { formartCurrency } from '@/utils';
import { toTypedSchema } from '@vee-validate/zod';
import { AxiosError } from 'axios';
import { useFieldArray, useForm } from 'vee-validate';
import { z } from 'zod';
import { addErrorFromServer } from '../../app/form/utils';
interface OrderItem {

  productId: string,
  image?: string
  name: string,
  currentQuantity: number,
  avaiableQuantity: number,
  unitPrice: number,
  salePrice: number | null | undefined
  status?: OrderItemStatus,
  itemTotalPrice: number
}

const props = defineProps<{ id: number, orderItems: Order['orderItems'] }>();
const emits = defineEmits<{ (e: 'paymentChange'): void }>();

const products = ref<Map<string, Product>>(await fetchProductsListToMap(props.orderItems.map(i => i.product.productCode)));
const oldItems = new Map<string, Order['orderItems'][number]>(new Map());

props.orderItems.forEach(o => {
  oldItems.set(o.product.productCode, o);
})

const schema = z.object({
  description: z.string().min(1),
  payload: z.object({
    orderItems: z.array(
      z.object({
        productId: z.string(),
        quantity: z.number().int(),
      }).refine(i => {
        const orderItem = oldItems.get(i.productId);
        if (orderItem) {
          if (i.quantity == 0 && orderItem.status === OrderItemStatus.NOT_SUPPLIED) {
            return true;
          }
          return i.quantity > 0;
        }
      }, {
        message: 'Số lượng sản phẩm phải lớn hơn 0',
        path: ['quantity']
      }).refine(i => {
        const allowcatedQuantity: number = oldItems.get(i.productId)?.suppliedQuantity || 0;
        const currentQuantity: number = i.quantity;
        const avaiableQuantityBackend: number = products.value.get(i.productId)!.quantity;

        if (avaiableQuantityBackend + allowcatedQuantity < currentQuantity) {
          return false;
        }

        return true;
      }, {
        message: "Sản phẩm trong kho không đủ",
        path: ['quantity']
      })
    ).min(1)
  })
})

const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: {
    payload: {
      orderItems: props.orderItems.map(o => ({ productId: o.product.productCode, quantity: o.suppliedQuantity }))
    }
  }
})

const { push: addOrder, remove: removeOrder, fields: currentItems } = useFieldArray<{ quantity: number, productId: string }>('payload.orderItems');

const isLoadProducts = ref<boolean>(false);

async function refreshProductData() {
  isLoadProducts.value = true;
  products.value = await fetchProductsListToMap(form.values.payload!.orderItems!.map(o => o.productId));
  isLoadProducts.value = false
}

const onSubmit = form.handleSubmit(async (values) => {
  try {
    await fetchOrderUpdateItem(props.id, values);
  } catch (error) {
    if (error instanceof AxiosError && error.status === 400) {
      addErrorFromServer(form, error.response?.data)
    }
    //TODO: Implemenet race Condiation Error
  }
})

const orderItems = computed<OrderItem[]>(() =>
  currentItems.value.map(o => {
    const currentItem = o.value;
    const product: Product | undefined = products.value.get(currentItem.productId);
    const oldItem: Order['orderItems'][number] | undefined = oldItems.get(currentItem.productId);
    if (!product) {
      return {
        productId: "",
        name: "Sản phẩm không còn tồn tại",
        currentQuantity: currentItem.quantity,
        avaiableQuantity: 0,
        unitPrice: oldItem?.unitPrice || 0,
        salePrice: oldItem?.salePrice,
        status: oldItem?.status,
        itemTotalPrice: currentItem.quantity * (oldItem?.salePrice || oldItem?.salePrice || 0)
      }
    }
    return {
      productId: currentItem.productId,
      name: product.name,
      image: product.mainImage?.src,
      currentQuantity: currentItem.quantity,
      avaiableQuantity: product.quantity + (oldItem?.suppliedQuantity || 0) - currentItem.quantity,
      unitPrice: product.originalPrice,
      salePrice: product.salePrice,
      status: oldItem?.status,
      itemTotalPrice: currentItem.quantity * (product.salePrice || product.originalPrice)
    }

  }) || []);

const totalPrice = computed<number>(() => orderItems.value.reduce((prev, curr) => prev + curr.itemTotalPrice, 0))
const isAddingItem = ref<boolean>(false);
const addingProductValue = ref<string>("");
const addingProductError = ref<string>();
const addingProductSchema = z.string().min(1, "Không được để trống")
  .refine(id => !currentItems.value.some(i => i.value.productId === id), {
    message: "Sản phẩm đã tồn tại"
  })

async function addProductToCart() {
  const productId: string = addingProductValue.value.trim();

  const validateResult = addingProductSchema.safeParse(productId);
  if (!validateResult.success) {
    addingProductError.value = validateResult.error.issues[0]?.message;
    return;
  }

  isAddingItem.value = true;
  const product = (await fetchProductsList([productId]))[0];
  if (product) {
    products.value.set(product.id, product);
    addOrder({ quantity: 1, productId })
    form.validateField('payload.orderItems')
    addingProductValue.value = "";
  } else {
    addingProductError.value = 'Sản phẩm không tồn tại'
  }
  isAddingItem.value = false;
}
</script>

<template>
  <DialogScrollContent class="sm:max-w-7xl" to="#dialog-container">
    <form class="flex gap-4 flex-col" @submit.prevent="onSubmit">
      <DialogHeader>
        <DialogTitle>Cập nhật thông tin sản phẩm</DialogTitle>
        <DialogDescription>
          Chú ý: Việc chỉnh sửa thêm bớt sẩn phẩm có thể làm mất đi giá khuyến mại tại thời điểm đặt đơn hàng của khách
          <br>
          Các sản phẩm được thêm vào giỏ hàng sẽ có dữ liệu mới nhất của sản phẩm đó
        </DialogDescription>
      </DialogHeader>

      <Separator class="mb-5 w-9/12 mx-auto" />
      <AppFormField name="payload.orderItems" label="Giỏ hàng: " is-required>
        <Table class="min-w-max border">
          <TableHeader>
            <TableRow>
              <TableHead>Mã SP</TableHead>
              <TableHead class="w-[100px]">
                Hình Ảnh
              </TableHead>
              <TableHead class="w-52">Tên SP</TableHead>
              <TableHead class="text-center">
                Số lượng
              </TableHead>
              <TableHead class="text-center">
                Còn lại
              </TableHead>
              <TableHead class="text-center">
                Đơn giá
              </TableHead>
              <TableHead class="text-center">
                Giá khuyến mại
              </TableHead>
              <TableHead class="text-center">
                Trạng thái cũ
              </TableHead>
              <TableHead class="text-center">
                Tổng
              </TableHead>
              <TableHead class="text-center">Hành động</TableHead>

            </TableRow>
          </TableHeader>
          <TableBody>
            <TableRow v-for="(item, index) in orderItems" :key="item.productId">
              <TableCell class="font-medium">
                {{ item.productId }}
              </TableCell>
              <TableCell>
                <img class="size-20 rounded-lg" :src="item.image || '/placeholder.svg'" />
              </TableCell>
              <TableCell>
                {{ item.name }}
              </TableCell>
              <TableCell>

                <FormField v-slot="{ componentField, setValue, value }" :name="`payload.orderItems[${index}].quantity`">
                  <FormItem class="w-full">
                    <FormControl>
                      <div class="flex gap-3">
                        <Button size="icon" variant="ghost" :disabled="item.avaiableQuantity <= 0" type="button"
                          @click="setValue(value + 1)">
                          <Icon icon="lucide:plus" />
                        </Button>
                        <Input type="number" v-bind="componentField" class="w-20 text-center" />
                        <Button size="icon" variant="ghost" :disabled="item.currentQuantity <= 1" type="button"
                          @click="setValue(value - 1)">
                          <Icon icon="lucide:minus" />
                        </Button>
                      </div>

                    </FormControl>

                    <FormMessage />
                  </FormItem>
                </FormField>


              </TableCell>
              <TableCell class="text-center">
                {{ item.avaiableQuantity }}
              </TableCell>
              <TableCell class="text-center">
                {{ formartCurrency(item.unitPrice) }}
              </TableCell>
              <TableCell class="text-center">
                {{ formartCurrency(item.salePrice) }}
              </TableCell>
              <TableCell>
                <Badge v-if="item.status === OrderItemStatus.FULLY_SUPPLIED" variant="outline">Đầy đủ</Badge>
                <Badge v-else-if="item.status === OrderItemStatus.PARTIALLY_SUPPLIED" variant="default">Bị thiếu
                </Badge>
                <Badge v-else-if="item.status === OrderItemStatus.NOT_SUPPLIED" variant="destructive">Hết hàng
                </Badge>
                <Badge v-else variant="outline">Mới</Badge>
              </TableCell>
              <TableCell>
                {{ formartCurrency(item.itemTotalPrice) }}
              </TableCell>
              <TableCell class="text-center">
                <Button size="icon" variant="destructive" type="button" @click="removeOrder(index)">
                  <Icon icon="lucide:trash" class="size-5" />
                </Button>
              </TableCell>
            </TableRow>
            <TableRow>
              <TableCell colspan="10">
                <div class="max-w-lg mx-auto flex flex-col gap-2">
                  <div class="flex  gap-2">
                    <Input name="add-item" v-model="addingProductValue" placeholder="Thêm sản phẩm: VD BPAK0304"
                      @keydown.enter.prevent="addProductToCart" :disabled="isAddingItem" />

                    <Button type="button" :disabled="isAddingItem" @click.prevent="addProductToCart">
                      <span v-if="!isAddingItem">Thêm</span>
                      <Icon v-else icon="lucide:loader" class="animate-spin" />
                    </Button>
                  </div>
                  <p v-if="addingProductError" class="text-[0.8rem] font-medium text-destructive">{{
                    addingProductError }}
                  </p>
                </div>
              </TableCell>
            </TableRow>
            <TableRow>
              <TableHead colspan="8" class="text-base">TỔNG: </TableHead>
              <TableCell colspan="2" class="text-base">{{ formartCurrency(totalPrice) }}</TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </AppFormField>
      <div>
        <AppFormFieldTextArea name="description" placeholder="Khách hàng muốn thay đổi ..."
          label="Ghi chú của nhân viên" is-required />
      </div>

      <DialogFooter>
        <Button variant="outline" type="button" @click="refreshProductData" :disable="isLoadProducts">

          <span v-if="!isLoadProducts"> Kiểm tra</span>
          <Icon v-else icon="lucide:loader" class="animate-spin" />
        </Button>
        <Button type="submit">
          Save changes
        </Button>
      </DialogFooter>
    </form>
  </DialogScrollContent>
</template>
<style>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}
</style>
