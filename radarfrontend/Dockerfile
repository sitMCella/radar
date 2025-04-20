# build environment
FROM node:23-alpine3.20 as build
COPY ./radarfrontend /app/
WORKDIR /app
RUN npm install
ENV PATH /app/node_modules/.bin:$PATH
RUN npm run build

# production environment
FROM nginx:1.27.5-alpine
COPY --from=build /app/build /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY ./radarfrontend/nginx.conf /etc/nginx/conf.d
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]