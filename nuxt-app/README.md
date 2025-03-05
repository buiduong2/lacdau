## Deploy

- Chúng ta cần thiết lập lại env ở phần NUxtConfig . VÌ ko hiểu tại sao nó lại không nhận env production

- Hiện tại chưa biết Build thế nào. Nên ta chạy trực tiếp như Dev Server bằng Production preview mà thôi

- Hiện tại khi mà ta cố gắng sử dụng npm run build thì no sẽ cố gắng load các static env trước khi ta sử dụng npm run preview. Nên ta cần phải thiết lập lại env

- B1. Thiết lập lại env trong file nuxt.config.ts


```js
		public: {
			authUrl: 'https://lacdau-auth.onrender.com',
			client_id: 'client-client-id',
			scope: 'openid',
			redirect_uri: 'https://lacdau-client.onrender.com/auth/callback',
			code_challenge_method: 'S256',
			authorizeUrl: 'https://lacdau-auth.onrender.com/oauth2/authorize'
		}
```

```sh
docker build -t duongbd1997/lacdau-client:latest .
```
