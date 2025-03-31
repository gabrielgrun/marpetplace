#!/bin/bash

yum update -y
yum install -y docker
systemctl start docker
systemctl enable docker

usermod -a -G docker ec2-user

curl -SL https://github.com/docker/compose/releases/download/v2.16.0/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

mkdir -p /home/ec2-user/app
cd /home/ec2-user/app

mkdir -p frontend

cat <<EOF > frontend/default.conf
server {
    listen 80;
    server_name localhost;
    client_max_body_size 20M;

    location / {
        root /usr/share/nginx/html;
        try_files \$uri \$uri/ /login.html;
    }

    location /api/ {
        proxy_pass http://api:8080/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF

cat <<EOF > frontend/nginx-global.conf
user nginx;
worker_processes  auto;
pid        /var/run/nginx.pid;

events {
    worker_connections  1024;
}

http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    sendfile        on;
    keepalive_timeout  65;
    include /etc/nginx/conf.d/*.conf;
}
EOF

cat <<'EOF' > docker-compose.yml
version: '3.8'
services:
  postgres:
    image: postgres:15
    container_name: postgres
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 123
      POSTGRES_DB: marpetplace
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - marpetplace

  api:
    image: gabrielgrun/marpetplace-backend:latest
    container_name: api
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/marpetplace
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: 123
    depends_on:
      - postgres
    ports:
      - "8080:8080"
    networks:
      - marpetplace

  frontend:
    image: gabrielgrun/marpetplace-frontend:latest
    container_name: frontend
    ports:
      - "80:80"
    # volumes:
    #   - ./frontend:/usr/share/nginx/html
    #   - ./frontend/default.conf:/etc/nginx/conf.d/default.conf
    #   - ./frontend/nginx-global.conf:/etc/nginx/nginx.conf
    networks:
      - marpetplace

networks:
  marpetplace:
    driver: bridge

volumes:
  postgres_data:
EOF

/usr/local/bin/docker-compose up -d
