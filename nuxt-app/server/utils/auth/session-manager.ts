import { InmemoryRepository } from './inmemory-repository'
import type { Session, SessionData, SessionManager } from './type'

export class InMemorySessionManager<T extends SessionData>
	implements SessionManager<T>
{
	private readonly repository: InmemoryRepository<Session<T>>

	public constructor() {
		this.repository = new InmemoryRepository(100)
	}

	public createSession(data: T): Session<T> {
		const session: Session<T> = new SessionEntity<T>(data)
		this.repository.save(session)
		return session;
	}

	public get(sessionId: string): Session<T> | undefined {
		return this.repository.get(sessionId)
	}

	public async delete(sessionId: string): Promise<void> {
		this.repository.delete(sessionId)
	}
}

class SessionEntity<T extends SessionData = SessionData> implements Session<T> {
	private id: string | undefined
	private data: T

	constructor(data: T) {
		this.data = data
	}

	getData(): T {
		return this.data
	}

	setData(data: T) {
		this.data = data
	}

	getId(): string {
		return this.id!
	}

	setId(id: string): void {
		this.id = id
	}
}
