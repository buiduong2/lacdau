import type { Entity } from './type'
const uniqueId = (prefix = 'id_') =>
	`${prefix}${Date.now()}${Math.random().toString(36).substring(2, 9)}`

export class InmemoryRepository<E extends Entity> {
	private map: Map<string, E>
	private queue: string[]
	private readonly maxSize: number

	public constructor(maxSize: number = 100) {
		this.maxSize = maxSize
		this.map = new Map()
		this.queue = []
	}

	public get(id: string): E | undefined {
		return this.map.get(id)
	}

	public save(e: E): E {
		const id = uniqueId()
		e.setId(id)
		this.map.set(id, e)
		this.queue.push(id)
		this.truncate()
		return e
	}

	public has(id: string): boolean {
		return this.map.has(id)
	}

	public delete(id: string) {
		this.map.delete(id)
	}

	private truncate(): void {
		if (this.queue.length > this.maxSize) {
			const topId = this.queue.shift()!
			this.map.delete(topId)
		}
	}
}
