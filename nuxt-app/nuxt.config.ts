// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
	compatibilityDate: '2024-04-03',
	devtools: { enabled: true },
	css: ['@/assets/scss/style.scss'],
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
