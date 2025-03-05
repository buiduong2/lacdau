import { z } from 'zod'

import {
    formDataForAccessToken,
    getClientHeaders
} from '~/server/utils/auth/oauth-helpers'
import { AuthUtils } from '~/server/utils/auth/auth-utils'
import { UserDetails } from '~/server/utils/auth/type'

const bodySchema = z.object({
	code: z
		.string({ required_error: 'not Null' })
		.min(1, { message: 'not empty' }),
	verifier: z
		.string({ required_error: 'not null' })
		.min(1, { message: 'not empty' })
})

export default defineEventHandler(async event => {
	const body = await readBody<{ code: string; verifier: string }>(event)
	const result = bodySchema.safeParse(body)
    
	if (result.success) {
		const res = await $fetch<TokenRes>(`${AUTH_URL}/oauth2/token`, {
			method: 'POST',
			body: formDataForAccessToken(body),
			headers: getClientHeaders()
		})

		const userDetails = await AuthUtils.login(event, res)

		return userDetails! as UserDetails
	} else {
		throw createError({ status: 400, message: 'BAD REQUEST' })
	}
})
