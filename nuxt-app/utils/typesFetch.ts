export interface Page<T> {
	content: T[]
	page: {
		size: number
		number: number
		totalElements: number
		totalPages: number
	}
}

export interface ProductSummaryRes {
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

export interface ProductDetailRes {
	id: string
	name: string
	viewCount: number
	specifications: string[]
	salePrice: number | null
	originalPrice: number
	categoryId: number
	thumbnails: Thumbnail[]
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
	productId:string
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
