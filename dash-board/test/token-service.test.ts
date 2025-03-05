import { describe, it, expect, vi, beforeEach } from 'vitest'
import { TokenService } from '../src/utils/token-service'
import type { TokenRes } from '../src/types/auth/resTypes'

const mockTokenSupplier = vi.fn().mockResolvedValue({
  access_token: 'newAccessToken',
  refresh_token: 'newRefreshToken',
  id_token: 'newIdToken',
  expires_in: 300,
})

function createTokenRes(expires_in: number): TokenRes {
  return {
    access_token: 'initialAccessToken',
    refresh_token: 'mockRefreshToken',
    id_token: 'initialIdToken',
    expires_in,
    scope: 'DASHBOARD',
    token_type: 'Bearer',
  }
}

describe('TokenService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should return the current access token if not expired', async () => {
    const tokenService = TokenService.createInstance(createTokenRes(10 * 60), mockTokenSupplier)

    const token = await tokenService.getAccessToken()
    expect(token).toBe('initialAccessToken')
  })

  it('should return the current id token if not expired', async () => {
    const tokenService = TokenService.createInstance(createTokenRes(10 * 60), mockTokenSupplier)

    const token = await tokenService.getIdToken()
    expect(token).toBe('initialIdToken')
  })

  // Hết hạn và được làm mới
  it('should refresh the token when expired and return the new access token', async () => {
    const tokenService = TokenService.createInstance(createTokenRes(-1), mockTokenSupplier)

    const token = await tokenService.getAccessToken()
    expect(mockTokenSupplier).toHaveBeenCalledOnce()
    expect(token).toBe('newAccessToken')
  })

  // 3. MutilRequest

  it('should process multiple pending requests while refreshing token', async () => {
    const tokenService = TokenService.createInstance(createTokenRes(-1), mockTokenSupplier)

    const promises = [
      tokenService.getAccessToken(),
      tokenService.getAccessToken(),
      tokenService.getAccessToken(),
    ]

    const tokens = await Promise.all(promises)
    expect(mockTokenSupplier).toHaveBeenCalledOnce() // Chỉ gọi API refresh token 1 lần
    expect(tokens.every((t) => t === 'newAccessToken')).toBe(true) // Các request khác sẽ nhận token mới
  })

  // Lỗi chờ bỏ qua
  it('should throw error when refresh token fails', async () => {
    const mockErrorSupplier = vi.fn().mockRejectedValue(new Error('Refresh token failed'))

    const tokenService = TokenService.createInstance(createTokenRes(-1), mockErrorSupplier)

    await expect(tokenService.getAccessToken()).rejects.toThrowError('Refresh token failed')
    expect(mockErrorSupplier).toHaveBeenCalledOnce() // Kiểm tra xem API refresh token bị gọi
  })

  it('should refresh the id token when expired and return the new id token', async () => {
    const tokenService = TokenService.createInstance(createTokenRes(-1), mockTokenSupplier)

    const token = await tokenService.getIdToken()
    expect(mockTokenSupplier).toHaveBeenCalledOnce() // Kiểm tra xem API refresh token có được gọi không
    expect(token).toBe('newIdToken') // Kiểm tra xem id token mới có đúng không
  })
})
