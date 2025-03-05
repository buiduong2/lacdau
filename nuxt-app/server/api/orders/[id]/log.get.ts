import { OrderLog } from "~/utils/typesFetch"

export default defineEventHandler<{}, Promise<OrderLog[]>>(async event => {
    const id = getRouterParams(event).id
    const accessToken: string | undefined = await AuthUtils.getAccessToken(
        event
    )

    if (accessToken) {
        return $fetch(`${RESOURCE_URL}/api/order/${id}/log`, {
            headers: getAuthHeaders(accessToken)
        })
    }

    AuthUtils.logout(event)
    throw createError({ status: 401, message: 'Unauthorized' })
})
