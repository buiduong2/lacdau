export default defineEventHandler(async event => {
	const idToken = await AuthUtils.getIdToken(event)
	if (idToken) {
        AuthUtils.logout(event);
		return generateOauthLogoutUrl(idToken)
	}

	throw createError({
		status: 401,
		message: 'UNAUTHORIZED'
	})
})
