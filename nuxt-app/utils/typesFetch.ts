export interface Page<T> {
	content: T[]
	page: {
		size: number
		number: number
		totalElements: number
		totalPages: number
	}
}

export interface ProductSummaryRes extends AddToCartAble {
	id: string
	name: string
	originalPrice: number
	salePrice: number | null
	quantity: number
	mainImage: ProductImage | null
}

export interface ProductImage {
	id: number
	src: string
	alt: string
}

export interface ProductDetailRes extends AddToCartAble {
	id: string
	name: string
	quantity: number
	viewCount: number
	specifications: string[]
	salePrice: number | null
	originalPrice: number
	categoryId: number
	thumbnails?: Thumbnail[]
	relatedProducts: RelateProduct[] | null
}

interface Thumbnail {
	id: number
	src: string
	alt: string
	position: number
}

interface RelateProduct {
	id: number
	productId: string
	name: string
	price: number
}

export interface FilterRes {
	price: {
		min: number
		max: number
	}
	brand: FilterGroup
	attributes: FilterGroup[]
}

interface FilterGroup {
	name: string
	attributeValues: {
		id: number
		name: string
	}[]
}

// Category

export interface CategoryRes {
	id: number
	name: string
	imageSrc: null | string
	parentId: null | number
}

export interface Profile {
	id: number

	displayName: string

	avatarUrl: string
}

// Province

export interface ProvinceBasic {
	id: number
	name: string
}

export interface Province {
	id: number
	name: string
	districts: {
		id: number
		name: string
	}[]
}

// Order

type OrderItemStatus = string
type OrderType = 'ONLINE' | 'IN_STORE'
export type OrderStage = string
type PaymentMethod = string
type PaymentStatus = string

interface AddToCartAble {
	quantity: number
	orderedQuantity?: number
}

export interface OrderDetailRes {
	id: number
	orderItems: {
		product: {
			name: string
			productCode: string
			mainImage: ProductImage | null
		}

		requestedQuantity: number
		suppliedQuantity: number
		unitPrice: number
		salePrice: number | null
		status: OrderItemStatus
	}[]

	stage: OrderStage
	type: OrderType
	payment: {
		paymentDate: string | null
		amountPaid: number | null
		totalItem: number
		method: PaymentMethod
		status: PaymentStatus
		shippingPrice: number | null
		subTotalPrice: number
		totalPrice: number | null
		createdAt: string
		updatedAt: string
	}
	shipment: {
		estimatedDelivery: string
		shippingPrice: number
		trackingDetail: string
		carrier: string
		createdAt: string
		updatedAt: string
	}
	orderAddress: {
		fullName: string
		phone: string
		province: string
		district: string
		detail: string
		message: string | null
	}
	createdAt: string
	updatedAt: string
}

export interface OrderSummaryRes {
	id: number
	type: OrderType
	stage: OrderStage
	payment: {
		totalItem: number
		method: PaymentMethod
		status: PaymentStatus
		amountPaid: number | null
		shippingPrice: number | null
		subTotalPrice: number
		totalPrice: number | null
	}

	createdAt: string
	updatedAt: string
}

export interface OrderLog {
	id: number
	orderId: number
	stage: OrderStage
	action: string
	description: string
	createdAt: string
	updatedAt: string
}

export interface OrderCreateReq {
	orderItems: {
		productId: string
		quantity: number
	}[]
	address: {
		fullName: string
		email: string
		phone: string
		districtId: number
		detail: string
		message?: string
	}

	paymentMethod: 'COD' | 'CARD' | 'CASH'
}

export interface CartItem {
	productId: string
	originalPrice: number
	salePrice: number | null
	quantity: number
	name: string
	totalPrice: number
	mainImage: ProductImage | null
	orderedQuantity: number
}

//Customer

type PersonStatus = string
type Gender = 'MALE' | 'FEMALE'

export interface Customer {
	id: number
	firstName: string
	lastName: string
	address: {
		id: number
		provinceId: number
		districtId: number
		province: string
		district: string
		detail: string
	}
	status: PersonStatus
	gender: Gender
	dob: string
	phone: string
	email: string
	userId: number
	createdAt: string
	updatedAt: string
}

export interface CustomerRegisterReq {
	firstName: string
	lastName: string
	gender: 'MALE' | 'FEMALE'
	dob: string
	phone: string
	email: string
	address: {
		districtId: number
		detail: string
	}
}
