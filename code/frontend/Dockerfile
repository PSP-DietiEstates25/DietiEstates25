# =========================
# 1) Build stage (Angular)
# =========================
FROM node:20-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .

RUN npm run build --configuration=production


# =========================
# 2) Runtime stage (Nginx)
# =========================
FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist/dietiestates25-frontend/browser/ /usr/share/nginx/html/

EXPOSE 80
