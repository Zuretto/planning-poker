FROM node:16 AS build

WORKDIR /app

ENV POKER_BACKEND_BASE_URL "http://localhost:8080"

COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . ./
RUN npm run build

FROM nginx:1.23-alpine
COPY --from=build /app/dist /usr/share/nginx/html