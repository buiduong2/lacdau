import type { TokenRes } from '@/types/auth/resTypes'

export async function fetchAccessToken(code: string, verifier: string): Promise<TokenRes> {
  const formData = new FormData()
  formData.append('code', code)
  formData.append('grant_type', 'authorization_code')

  formData.append('redirect_uri', oauthConfig.redirect_uri)
  formData.append('code_verifier', verifier)

  return fetchClientOAuth(oauthConfig.authUrl + '/oauth2/token', formData)
}

export function fetchRefreshAccessToken(refreshToken: string): Promise<TokenRes> {
  const formData = new FormData()
  formData.append('grant_type', 'refresh_token')
  formData.append('refresh_token', refreshToken)

  return fetchClientOAuth(oauthConfig.authUrl + '/oauth2/token', formData)
}

export async function revokeRefreshToken(refreshToken: string) {
  const formData = new FormData()
  formData.append('token', refreshToken)
  return fetchClientOAuth(oauthConfig.authUrl + '/oauth2/revoke', formData, false)
}

async function fetchClientOAuth(url: string, formData: FormData, body: boolean = true) {
  const res = await fetch(url, {
    method: 'POST',
    headers: {
      ...getClientHeaders(),
    },
    body: formData,
  })

  if (res.ok) {
    if (body) {
      return res.json()
    }
    return
  }

  throw res
}
