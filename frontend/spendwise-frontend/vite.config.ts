import { defineConfig } from 'vite'

export default defineConfig({
  build: {
    rollupOptions: {
      input: {
        index: 'index.html',
        login: './src/pages/auth/login/login.html',
        register: './src/pages/auth/register/register.html',
        dashboard: './src/pages/dashboard/dashboard.html',
        new_category: './src/pages/new_category/new_category.html',
        new_transaction: './src/pages/new_traction/new_transaction.html'
      }
    }
  }
})