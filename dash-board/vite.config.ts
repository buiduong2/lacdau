import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
//CSS
import autoprefixer from 'autoprefixer'
import tailwind from 'tailwindcss'
import vueJsx from '@vitejs/plugin-vue-jsx'

//Auto import
import { VueRouterAutoImports } from 'unplugin-vue-router'
import AutoImport from 'unplugin-auto-import/vite'
import VueRouter from 'unplugin-vue-router/vite'
import Components from 'unplugin-vue-components/vite'

// https://vite.dev/config/
export default defineConfig({
  css: {
    postcss: {
      plugins: [tailwind(), autoprefixer()],
    },
  },
  plugins: [
    vue(),
    VueRouter(),
    AutoImport({
      include: [
        /\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
        /\.vue$/,
        /\.vue\?vue/, // .vue
        /\.md$/, // .md
      ],
      dts: true,

      viteOptimizeDeps: true,
      dirs: ['src/stores', 'src/utils', 'src/composables', 'src/apis/**'],

      imports: ['vue', 'vue-router', VueRouterAutoImports],
    }),
    Components({
      dts: true,
      resolvers: [
        (componentName) => {
          if (componentName === 'Icon')
            return { name: componentName, from: '@iconify/vue/dist/iconify.js' }
        },
      ],
    }),

    vueJsx(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
})
