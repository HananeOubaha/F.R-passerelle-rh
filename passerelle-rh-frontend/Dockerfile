# ---- Stage 1: Build ----
FROM node:20-alpine AS builder
WORKDIR /app

COPY package*.json ./
RUN npm install --legacy-peer-deps

COPY . .
RUN npm run build -- --configuration production

# ---- Stage 2: Serve with Nginx ----
FROM nginx:alpine
COPY --from=builder /app/dist/passerelle-rh-frontend/browser /usr/share/nginx/html

# Nginx config for Angular (handles client-side routing)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
