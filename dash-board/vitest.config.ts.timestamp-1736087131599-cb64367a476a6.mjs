// vitest.config.ts
import { fileURLToPath as fileURLToPath2 } from "node:url";
import { mergeConfig, defineConfig as defineConfig2, configDefaults } from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/vitest/dist/config.js";

// vite.config.ts
import { fileURLToPath, URL as URL2 } from "node:url";
import { defineConfig } from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/vite/dist/node/index.js";
import vue from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import vueDevTools from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/vite-plugin-vue-devtools/dist/vite.mjs";
import autoprefixer from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/autoprefixer/lib/autoprefixer.js";
import tailwind from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/tailwindcss/lib/index.js";
import vueJsx from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/@vitejs/plugin-vue-jsx/dist/index.mjs";
import { VueRouterAutoImports } from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/unplugin-vue-router/dist/index.js";
import AutoImport from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/unplugin-auto-import/dist/vite.js";
import VueRouter from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/unplugin-vue-router/dist/vite.js";
import Components from "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/node_modules/unplugin-vue-components/dist/vite.js";
var __vite_injected_original_import_meta_url = "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/vite.config.ts";
var vite_config_default = defineConfig({
  css: {
    postcss: {
      plugins: [tailwind(), autoprefixer()]
    }
  },
  plugins: [
    vue(),
    VueRouter(),
    AutoImport({
      include: [
        /\.[tj]sx?$/,
        // .ts, .tsx, .js, .jsx
        /\.vue$/,
        /\.vue\?vue/,
        // .vue
        /\.md$/
        // .md
      ],
      dts: true,
      viteOptimizeDeps: true,
      dirs: ["src/stores", "src/utils", "src/composables", "src/apis/**"],
      imports: ["vue", "vue-router", VueRouterAutoImports]
    }),
    Components({
      dts: true,
      resolvers: [
        (componentName) => {
          if (componentName === "Icon")
            return { name: componentName, from: "@iconify/vue/dist/iconify.js" };
        }
      ]
    }),
    vueJsx(),
    vueDevTools()
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL2("./src", __vite_injected_original_import_meta_url))
    }
  }
});

// vitest.config.ts
var __vite_injected_original_import_meta_url2 = "file:///D:/03-Code/09-Spring/project-2024/shop/dash-board/vitest.config.ts";
var vitest_config_default = mergeConfig(
  vite_config_default,
  defineConfig2({
    test: {
      environment: "jsdom",
      exclude: [...configDefaults.exclude, "e2e/**"],
      root: fileURLToPath2(new URL("./", __vite_injected_original_import_meta_url2))
    }
  })
);
export {
  vitest_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZXN0LmNvbmZpZy50cyIsICJ2aXRlLmNvbmZpZy50cyJdLAogICJzb3VyY2VzQ29udGVudCI6IFsiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkQ6XFxcXDAzLUNvZGVcXFxcMDktU3ByaW5nXFxcXHByb2plY3QtMjAyNFxcXFxzaG9wXFxcXGRhc2gtYm9hcmRcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkQ6XFxcXDAzLUNvZGVcXFxcMDktU3ByaW5nXFxcXHByb2plY3QtMjAyNFxcXFxzaG9wXFxcXGRhc2gtYm9hcmRcXFxcdml0ZXN0LmNvbmZpZy50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vRDovMDMtQ29kZS8wOS1TcHJpbmcvcHJvamVjdC0yMDI0L3Nob3AvZGFzaC1ib2FyZC92aXRlc3QuY29uZmlnLnRzXCI7aW1wb3J0IHsgZmlsZVVSTFRvUGF0aCB9IGZyb20gJ25vZGU6dXJsJ1xuaW1wb3J0IHsgbWVyZ2VDb25maWcsIGRlZmluZUNvbmZpZywgY29uZmlnRGVmYXVsdHMgfSBmcm9tICd2aXRlc3QvY29uZmlnJ1xuaW1wb3J0IHZpdGVDb25maWcgZnJvbSAnLi92aXRlLmNvbmZpZydcblxuZXhwb3J0IGRlZmF1bHQgbWVyZ2VDb25maWcoXG4gIHZpdGVDb25maWcsXG4gIGRlZmluZUNvbmZpZyh7XG4gICAgdGVzdDoge1xuICAgICAgZW52aXJvbm1lbnQ6ICdqc2RvbScsXG4gICAgICBleGNsdWRlOiBbLi4uY29uZmlnRGVmYXVsdHMuZXhjbHVkZSwgJ2UyZS8qKiddLFxuICAgICAgcm9vdDogZmlsZVVSTFRvUGF0aChuZXcgVVJMKCcuLycsIGltcG9ydC5tZXRhLnVybCkpLFxuICAgIH0sXG4gIH0pLFxuKVxuIiwgImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJEOlxcXFwwMy1Db2RlXFxcXDA5LVNwcmluZ1xcXFxwcm9qZWN0LTIwMjRcXFxcc2hvcFxcXFxkYXNoLWJvYXJkXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJEOlxcXFwwMy1Db2RlXFxcXDA5LVNwcmluZ1xcXFxwcm9qZWN0LTIwMjRcXFxcc2hvcFxcXFxkYXNoLWJvYXJkXFxcXHZpdGUuY29uZmlnLnRzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9EOi8wMy1Db2RlLzA5LVNwcmluZy9wcm9qZWN0LTIwMjQvc2hvcC9kYXNoLWJvYXJkL3ZpdGUuY29uZmlnLnRzXCI7aW1wb3J0IHsgZmlsZVVSTFRvUGF0aCwgVVJMIH0gZnJvbSAnbm9kZTp1cmwnXG5pbXBvcnQgeyBkZWZpbmVDb25maWcgfSBmcm9tICd2aXRlJ1xuaW1wb3J0IHZ1ZSBmcm9tICdAdml0ZWpzL3BsdWdpbi12dWUnXG5pbXBvcnQgdnVlRGV2VG9vbHMgZnJvbSAndml0ZS1wbHVnaW4tdnVlLWRldnRvb2xzJ1xuLy9DU1NcbmltcG9ydCBhdXRvcHJlZml4ZXIgZnJvbSAnYXV0b3ByZWZpeGVyJ1xuaW1wb3J0IHRhaWx3aW5kIGZyb20gJ3RhaWx3aW5kY3NzJ1xuaW1wb3J0IHZ1ZUpzeCBmcm9tICdAdml0ZWpzL3BsdWdpbi12dWUtanN4J1xuXG4vL0F1dG8gaW1wb3J0XG5pbXBvcnQgeyBWdWVSb3V0ZXJBdXRvSW1wb3J0cyB9IGZyb20gJ3VucGx1Z2luLXZ1ZS1yb3V0ZXInXG5pbXBvcnQgQXV0b0ltcG9ydCBmcm9tICd1bnBsdWdpbi1hdXRvLWltcG9ydC92aXRlJ1xuaW1wb3J0IFZ1ZVJvdXRlciBmcm9tICd1bnBsdWdpbi12dWUtcm91dGVyL3ZpdGUnXG5pbXBvcnQgQ29tcG9uZW50cyBmcm9tICd1bnBsdWdpbi12dWUtY29tcG9uZW50cy92aXRlJ1xuXG4vLyBodHRwczovL3ZpdGUuZGV2L2NvbmZpZy9cbmV4cG9ydCBkZWZhdWx0IGRlZmluZUNvbmZpZyh7XG4gIGNzczoge1xuICAgIHBvc3Rjc3M6IHtcbiAgICAgIHBsdWdpbnM6IFt0YWlsd2luZCgpLCBhdXRvcHJlZml4ZXIoKV0sXG4gICAgfSxcbiAgfSxcbiAgcGx1Z2luczogW1xuICAgIHZ1ZSgpLFxuICAgIFZ1ZVJvdXRlcigpLFxuICAgIEF1dG9JbXBvcnQoe1xuICAgICAgaW5jbHVkZTogW1xuICAgICAgICAvXFwuW3RqXXN4PyQvLCAvLyAudHMsIC50c3gsIC5qcywgLmpzeFxuICAgICAgICAvXFwudnVlJC8sXG4gICAgICAgIC9cXC52dWVcXD92dWUvLCAvLyAudnVlXG4gICAgICAgIC9cXC5tZCQvLCAvLyAubWRcbiAgICAgIF0sXG4gICAgICBkdHM6IHRydWUsXG5cbiAgICAgIHZpdGVPcHRpbWl6ZURlcHM6IHRydWUsXG4gICAgICBkaXJzOiBbJ3NyYy9zdG9yZXMnLCAnc3JjL3V0aWxzJywgJ3NyYy9jb21wb3NhYmxlcycsICdzcmMvYXBpcy8qKiddLFxuXG4gICAgICBpbXBvcnRzOiBbJ3Z1ZScsICd2dWUtcm91dGVyJywgVnVlUm91dGVyQXV0b0ltcG9ydHNdLFxuICAgIH0pLFxuICAgIENvbXBvbmVudHMoe1xuICAgICAgZHRzOiB0cnVlLFxuICAgICAgcmVzb2x2ZXJzOiBbXG4gICAgICAgIChjb21wb25lbnROYW1lKSA9PiB7XG4gICAgICAgICAgaWYgKGNvbXBvbmVudE5hbWUgPT09ICdJY29uJylcbiAgICAgICAgICAgIHJldHVybiB7IG5hbWU6IGNvbXBvbmVudE5hbWUsIGZyb206ICdAaWNvbmlmeS92dWUvZGlzdC9pY29uaWZ5LmpzJyB9XG4gICAgICAgIH0sXG4gICAgICBdLFxuICAgIH0pLFxuXG4gICAgdnVlSnN4KCksXG4gICAgdnVlRGV2VG9vbHMoKSxcbiAgXSxcbiAgcmVzb2x2ZToge1xuICAgIGFsaWFzOiB7XG4gICAgICAnQCc6IGZpbGVVUkxUb1BhdGgobmV3IFVSTCgnLi9zcmMnLCBpbXBvcnQubWV0YS51cmwpKSxcbiAgICB9LFxuICB9LFxufSlcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFBcVYsU0FBUyxpQkFBQUEsc0JBQXFCO0FBQ25YLFNBQVMsYUFBYSxnQkFBQUMsZUFBYyxzQkFBc0I7OztBQ0R1UixTQUFTLGVBQWUsT0FBQUMsWUFBVztBQUNwWCxTQUFTLG9CQUFvQjtBQUM3QixPQUFPLFNBQVM7QUFDaEIsT0FBTyxpQkFBaUI7QUFFeEIsT0FBTyxrQkFBa0I7QUFDekIsT0FBTyxjQUFjO0FBQ3JCLE9BQU8sWUFBWTtBQUduQixTQUFTLDRCQUE0QjtBQUNyQyxPQUFPLGdCQUFnQjtBQUN2QixPQUFPLGVBQWU7QUFDdEIsT0FBTyxnQkFBZ0I7QUFiOEwsSUFBTSwyQ0FBMkM7QUFnQnRRLElBQU8sc0JBQVEsYUFBYTtBQUFBLEVBQzFCLEtBQUs7QUFBQSxJQUNILFNBQVM7QUFBQSxNQUNQLFNBQVMsQ0FBQyxTQUFTLEdBQUcsYUFBYSxDQUFDO0FBQUEsSUFDdEM7QUFBQSxFQUNGO0FBQUEsRUFDQSxTQUFTO0FBQUEsSUFDUCxJQUFJO0FBQUEsSUFDSixVQUFVO0FBQUEsSUFDVixXQUFXO0FBQUEsTUFDVCxTQUFTO0FBQUEsUUFDUDtBQUFBO0FBQUEsUUFDQTtBQUFBLFFBQ0E7QUFBQTtBQUFBLFFBQ0E7QUFBQTtBQUFBLE1BQ0Y7QUFBQSxNQUNBLEtBQUs7QUFBQSxNQUVMLGtCQUFrQjtBQUFBLE1BQ2xCLE1BQU0sQ0FBQyxjQUFjLGFBQWEsbUJBQW1CLGFBQWE7QUFBQSxNQUVsRSxTQUFTLENBQUMsT0FBTyxjQUFjLG9CQUFvQjtBQUFBLElBQ3JELENBQUM7QUFBQSxJQUNELFdBQVc7QUFBQSxNQUNULEtBQUs7QUFBQSxNQUNMLFdBQVc7QUFBQSxRQUNULENBQUMsa0JBQWtCO0FBQ2pCLGNBQUksa0JBQWtCO0FBQ3BCLG1CQUFPLEVBQUUsTUFBTSxlQUFlLE1BQU0sK0JBQStCO0FBQUEsUUFDdkU7QUFBQSxNQUNGO0FBQUEsSUFDRixDQUFDO0FBQUEsSUFFRCxPQUFPO0FBQUEsSUFDUCxZQUFZO0FBQUEsRUFDZDtBQUFBLEVBQ0EsU0FBUztBQUFBLElBQ1AsT0FBTztBQUFBLE1BQ0wsS0FBSyxjQUFjLElBQUlDLEtBQUksU0FBUyx3Q0FBZSxDQUFDO0FBQUEsSUFDdEQ7QUFBQSxFQUNGO0FBQ0YsQ0FBQzs7O0FEekRzTixJQUFNQyw0Q0FBMkM7QUFJeFEsSUFBTyx3QkFBUTtBQUFBLEVBQ2I7QUFBQSxFQUNBQyxjQUFhO0FBQUEsSUFDWCxNQUFNO0FBQUEsTUFDSixhQUFhO0FBQUEsTUFDYixTQUFTLENBQUMsR0FBRyxlQUFlLFNBQVMsUUFBUTtBQUFBLE1BQzdDLE1BQU1DLGVBQWMsSUFBSSxJQUFJLE1BQU1GLHlDQUFlLENBQUM7QUFBQSxJQUNwRDtBQUFBLEVBQ0YsQ0FBQztBQUNIOyIsCiAgIm5hbWVzIjogWyJmaWxlVVJMVG9QYXRoIiwgImRlZmluZUNvbmZpZyIsICJVUkwiLCAiVVJMIiwgIl9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwiLCAiZGVmaW5lQ29uZmlnIiwgImZpbGVVUkxUb1BhdGgiXQp9Cg==
