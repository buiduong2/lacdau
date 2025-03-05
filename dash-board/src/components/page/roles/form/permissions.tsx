import type { Authority } from '@/types/auth/resTypes'

export const permissionsMeta: Record<Authority, { label: string; desc: string; icon: string }> = {
  CUSTOMER_MANAGE: {
    label: 'Quản lý khách hàng',
    icon: 'lucide:circle-user-round',
    desc: 'Chỉnh sửa, thêm, xóa thông tin khác hàng',
  },
  ORDER_MANAGE: {
    label: 'Quản lý bán hàng',
    icon: 'lucide:shopping-cart',
    desc: 'Chỉnh sửa, thêm, xóa các đơn hàng được yêu cầu ...',
  },
  PRODUCT_MANAGE: {
    label: 'Quản lý sản phẩm',
    icon: 'lucide:boxes',
    desc: 'Chỉnh sửa, thêm, xóa thông tin sản phẩm, thuộc tính, danh mục ...',
  },
  USER_MANAGE: {
    label: 'Quản lý người dùng',
    icon: 'lucide:user-round-cog',
    desc: 'Chỉnh sửa, cấp quyền các người dùng nội bộ',
  },
  REPORT_VIEW: {
    label: 'Quản lý báo cáo',
    icon: 'lucide:chart-no-axes-combined',
    desc: 'Xem các báo cáo doanh thu',
  },
}
