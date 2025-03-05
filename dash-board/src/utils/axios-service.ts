import type { AxiosInstance } from 'axios'
import axios from 'axios'
import type { TokenService } from './token-service'
const resourceURL = import.meta.env.VITE_RESOURCE_URL
export class AxiosService {
  private authClient: AxiosInstance
  private resourceClient: AxiosInstance
  private static instance: AxiosService
  private tokenService: TokenService
  private constructor(tokenService: TokenService) {
    this.authClient = axios.create({ baseURL: oauthConfig.authUrl, timeout: 5000 })
    this.resourceClient = axios.create({ baseURL: resourceURL, timeout: 5000 })
    this.tokenService = tokenService
    this.initAxiosInstance()
  }

  private initAxiosInstance() {
    this.addInterceptorForGetAccessToken()
  }

  private addInterceptorForGetAccessToken() {
    const clients = [this.authClient, this.resourceClient]
    const tokenService = this.tokenService
    clients.forEach((client) => {
      client.interceptors.request.use(async function (config) {
        const token = await tokenService.getAccessToken()

        if (token) {
          config.headers.Authorization = `Bearer ${token}`
        }
        return config
      })
    })
  }

  public static createInstance(tokenService: TokenService) {
    this.instance = new AxiosService(tokenService)
  }

  public static getInstance() {
    if (this.instance) {
      return this.instance
    }
    throw new Error('axios Service must be create before access')
  }

  public getAuthClient(): AxiosService['authClient'] {
    return this.authClient
  }

  public getResourceClient(): AxiosService['resourceClient'] {
    return this.resourceClient
  }
}
