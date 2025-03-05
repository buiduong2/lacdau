// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
	compatibilityDate: '2024-04-03',
	devtools: { enabled: true },
	css: ['@/assets/scss/style.scss'],
	runtimeConfig: {
		// public: {
		//     authUrl: process.env.PUBLIC_AUTH_API_URL,
		// 	client_id: process.env.NUXT_PUBLIC_AUTH_CLIENT_ID,
		// 	scope:process.env.NUXT_PUBLIC_AUTH_SCOPE,
		// 	redirect_uri: process.env.NUXT_PUBLIC_AUTH_LOGIN_CALLBACK,
		// 	code_challenge_method: 'S256',
		// 	authorizeUrl: process.env.NUXT_PUBLIC_AUTH_AUTHORIZE_URL,
		// },
		public: {
			authUrl: 'https://lacdau-auth.onrender.com',
			client_id: 'client-client-id',
			scope: 'openid',
			redirect_uri: 'https://lacdau-client.onrender.com/auth/callback',
			code_challenge_method: 'S256',
			authorizeUrl: 'https://lacdau-auth.onrender.com/oauth2/authorize'
		}
	},
	experimental: {
		typedPages: true
	},

	vite: {
		css: {
			preprocessorOptions: {
				scss: {
					api: 'modern-compiler'
				}
			}
		}
	},

	modules: [
		[
			'@nuxtjs/google-fonts',
			{
				families: {
					Mulish: {
						wght: '200..900',
						ital: '200..900'
					}
				}
			}
		],
		'@nuxt/test-utils/module',
		'@nuxt/image',
		'@pinia/nuxt'
	]
})
