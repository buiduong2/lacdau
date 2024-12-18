# Sử dụng image Nginx làm base image
FROM nginx:alpine

# Copy các file từ thư mục dist vào thư mục /usr/share/nginx/html của container
COPY dist/ /usr/share/nginx/html/

# Copy cấu hình nginx custom vào container
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80
EXPOSE 80

# Khởi chạy nginx khi container chạy
CMD ["nginx", "-g", "daemon off;"]