export default defineEventHandler<
	{ body: { sessionId: string } },
	ReturnType<typeof AuthUtils.authenticate>
>(async event => {
	const body = await readBody<{ sessionId: string }>(event)

	return AuthUtils.authenticate(body.sessionId)
})
