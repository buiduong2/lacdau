type Option = {
	liveTime?: number
	isSuccess?: boolean
}
type Injected = {
	notification: (msg: string, option?: Option) => Promise<void>
}
export function useNotification() {
	const injected = inject<Injected>('notification')

	if (!injected) {
		throw new Error('<AppNotification /> must be place in parent component')
	}

	return {
		notification: injected.notification
	}
}
