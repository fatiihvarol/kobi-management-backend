# SubsManagement - Auth Service

Bu proje, mikroservis mimarisinde authentication servisini içermektedir.

## Kurulum

### 1. Environment Variables Ayarlama

Projeyi çalıştırmadan önce environment variables'ları ayarlamanız gerekiyor:

```bash
# env.example dosyasını .env olarak kopyalayın
cp env.example .env
```

`.env` dosyasını düzenleyerek kendi değerlerinizi girin:

```env
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://auth-db:5432/authdb
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update

# RabbitMQ Configuration
RABBITMQ_HOST=rabbitmq
RABBITMQ_USERNAME=your_rabbitmq_username
RABBITMQ_PASSWORD=your_rabbitmq_password

# JWT Configuration
JWT_SECRET=your_very_secure_jwt_secret_key
JWT_EXPIRATION_MS=86400000

# PostgreSQL Database Configuration
POSTGRES_DB=authdb
POSTGRES_USER=your_db_username
POSTGRES_PASSWORD=your_db_password
```

### 2. Docker ile Çalıştırma

```bash
# Tüm servisleri başlat
docker-compose up -d

# Logları görüntüle
docker-compose logs -f auth-service
```

### 3. Servisler

- **Auth Service**: http://localhost:8080
- **PostgreSQL**: localhost:5432
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)

## API Endpoints

### Authentication
- `POST /api/auth/signup` - Kullanıcı kaydı
- `POST /api/auth/signin` - Kullanıcı girişi

## Güvenlik

- Tüm hassas bilgiler (şifreler, JWT secret) environment variables ile yönetilmektedir
- `.env` dosyası `.gitignore`'da bulunmaktadır ve GitHub'a yüklenmez
- Production ortamında güçlü şifreler kullanın

## Geliştirme

### Local Development
```bash
# Sadece veritabanı ve RabbitMQ'yu başlat
docker-compose up -d auth-db rabbitmq

# Auth service'i IDE'de çalıştır
```

### Debugging
- Debug port: 5005
- IDE'de remote debugging ayarlayın
