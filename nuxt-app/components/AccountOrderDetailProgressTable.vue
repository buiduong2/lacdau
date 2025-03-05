<script setup lang="ts">
const props = defineProps<{ orderLogs: OrderLog[]; order: OrderDetailRes }>()

const stages: { stage: OrderStage; desc: string }[] = [
	{ stage: 'PENDING', desc: 'Đơn hàng đã được đặt' },
	{ stage: 'CONFIRMATION', desc: 'Đơn hàng đang được xác nhận' },
	{ stage: 'PAYMENT', desc: 'Đang đợi thanh toán đơn hàng' },
	{ stage: 'PACKING', desc: 'Cửa hàng đang chuẩn bị hàng' },
	{ stage: 'SHIPPING', desc: 'Đơn hàng đang được giao' },
	{ stage: 'DELIVERED', desc: 'Đơn hàng đã giao tới nơi' },
	{ stage: 'SUCCESSFUL', desc: 'Đơn hàng đã giao thành công' },
	{ stage: 'COMPLETED', desc: 'Đơn hàng đã thành công' }
]

const cancelStages: { stage: OrderStage; desc: string }[] = [
	{ stage: 'FAILURE', desc: 'Đơn hàng thất bại' },
	{ stage: 'CANCELED', desc: 'Đơn hàng đã được hủy' }
]

const logMap = computed<Map<string, OrderLog>>(() => {
	const map = new Map<string, OrderLog>()
	props.orderLogs.forEach(log => {
		map.set(log.stage, log)
	})

	return map
})
const isCanceled = computed(() =>
	cancelStages.some(stage => stage.stage === props.order.stage)
)
</script>
<template>
	<table class="order-info-table">
		<tbody>
			<tr>
				<th>Đang thực thi</th>
			</tr>
			<tr v-for="stage in stages" :key="stage.stage">
				<th
					:class="
						logMap.has(stage.stage)
							? order.stage === stage.stage
								? 'text-primary'
								: 'text-success'
							: 'text-secondary'
					"
				>
					{{
						logMap.has(stage.stage)
							? formatDate(logMap.get(stage.stage)!.createdAt)
							: 'Chưa có thông tin'
					}}
				</th>
				<td
					:class="
						logMap.has(stage.stage)
							? order.stage === stage.stage
								? 'text-primary'
								: 'text-success'
							: 'text-secondary'
					"
				>
					{{ stage.desc }}
				</td>
			</tr>

			<template v-if="isCanceled">
			    <tr>
    				<th>Đã hủy</th>
    			</tr>
    
    			<tr v-for="stage in cancelStages" :key="stage.stage">
    				<th
    					:class="
    						logMap.has(stage.stage)
    							? order.stage === stage.stage
    								? 'text-danger'
    								: 'text-warning'
    							: 'text-secondary'
    					"
    				>
    					{{
    						logMap.has(stage.stage)
    							? formatDate(logMap.get(stage.stage)!.createdAt)
    							: 'Chưa có thông tin'
    					}}
    				</th>
    				<td
    					:class="
    						logMap.has(stage.stage)
    							? order.stage === stage.stage
    								? 'text-danger'
    								: 'text-warning'
    							: 'text-secondary'
    					"
    				>
    					{{ stage.desc }}
    				</td>
    			</tr>
			</template>
		</tbody>
	</table>
</template>

<style scoped></style>
