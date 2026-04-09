# Turkcell CRM - Microservices Backend

Bu proje, modern yazılım mimarisi prensipleri temel alınarak geliştirilmiş, **Spring Boot ve Spring Cloud** altyapısını kullanan kapsamlı bir **Mikroservis (Microservices)** projesidir. Proje, bir CRM (Müşteri İlişkileri Yönetimi) ve E-Ticaret sisteminin temel ihtiyaçlarını karşılayacak modüllerden oluşmaktadır. Dağıtık mimari, asenkron iletişim, merkezi konfigürasyon ve API Gateway gibi gelişmiş konseptler içerir.

## 🚀 Teknolojiler ve Araçlar

Proje genelinde kullanılan temel teknolojiler şunlardır:

### Backend ve Framework
- **Java 17**
- **Spring Boot 3.2.x** (Web, Data JPA, Validation)
- **Spring Cloud 2023.0.x** (Eureka, Config, Gateway MVC, OpenFeign)
- **Spring Security** (Kimlik Doğrulama ve Yetkilendirme)

### Veritabanları ve Önbellek
- **PostgreSQL**: İlişkisel veri saklama ihtiyaçları için (Account, Customer, Identity, vs.).
- **MongoDB**: Doküman tabanlı hızlı arama/okuma ihtiyaçları için (Search Service).
- **Redis**: Önbellekleme (Caching) ve performans artışı için (Docker-compose'da hazır yapılandırılmıştır).

### Mesajlaşma (Event-Driven Architecture)
- **Apache Kafka**: Servisler arası asenkron iletişim ve olay güdümlü (event-driven) mimari altyapısı için.
- **Zookeeper**: Kafka'nın yönetimi için.
- **Kafka-UI**: Kafka topic'lerini ve mesajlarını görsel olarak izleyebilmek için.

### Altyapı ve DevOps
- **Docker & Docker Compose**: Tüm servislerin ve altyapı bileşenlerinin (DB'ler, Kafka) konteynerize çalıştırılması.
- **Maven**: Bağımlılık yönetimi ve derleme.
- **Lombok**: Boilerplate (tekrar eden) kodları azaltmak.
- **MapStruct**: (Opsiyonel/Yaygın) DTO ve Entity dönüşümleri için.
- **Swagger / OpenAPI**: API dokümantasyonu (`springdoc-openapi`).
- **io.github.yozyazici:common-package (v1.0.1)**: Servisler arası ortak kullanılan sınıflar, DTO'lar veya exception'ları barındıran custom (özel) paket.

---

## 🏗️ Mikroservis Mimarisi

Sistem, sorumlulukların ayrıştırıldığı (Separation of Concerns) bağımsız servislerden oluşmaktadır:

### ⚙️ Altyapı Servisleri (Infrastructure)
* **`discoveryServer` (Eureka Server - Port 8761):** Tüm servislerin kendini kayıt ettiği ve birbirlerini bulduğu (Service Discovery) sunucudur. Diğer servisler (Eureka Client) ayağa kalktıklarında buraya IP/Port bilgilerini bildirir.
* **`configServer` (Port 8051):** Mikroservislerin konfigürasyon (application.yml/properties) ayarlarını merkezi bir noktadan yönetir. Servisler ayağa kalkarken ayarlarını buradan çeker.
* **`gatewayService` (Spring Cloud Gateway MVC - Port 8760):** İstemcilerden (Frontend/Mobil) gelen tüm istekleri karşılayan tek giriş noktasıdır (Single Point of Entry). İstekleri uygun mikroservislere yönlendirir (Routing) ve gerektiğinde filtreleme yapar.

### 💼 İş (Business) Servisleri
* **`identityService` (Port 8763):** Kullanıcı kaydı, giriş işlemleri, Spring Security ile kimlik doğrulama, token (JWT vs.) üretimi ve yetkilendirmeleri yönetir (PostgreSQL kullanır).
* **`accountService` (Port 8764):** Kullanıcıların hesap bilgilerini ve detaylarını tutar. Veritabanı PostgreSQL'dir.
* **`customerService`:** Müşteri temel profillerini ve müşteriyle ilgili iş süreçlerini yönetir (PostgreSQL kullanır). Kafka üzerinden event fırlatıp dinleyebilir.
* **`orderService`:** Sipariş süreçlerinin yönetildiği servistir. Senkron iletişim için **OpenFeign** kullanılarak diğer servislerle iletişim kurar.
* **`basketService`:** E-ticaret sepet işlemlerini yönetir (Genelde Redis veya hızlı bir DB kullanır).
* **`catalogService`:** Ürün, tarife veya hizmetlerin kataloglanmasını sağlar.
* **`searchService`:** Sistemdeki arama işlemlerinin (müşteri bulma, ürün arama vs.) çok hızlı yapılabilmesi için kullanılan servistir. İlişkisel veritabanlarını yormamak adına **MongoDB** kullanır. Diğer servislerdeki veriler Kafka üzerinden dinlenerek buradaki MongoDB'ye senkronize edilir (CQRS desenine yatkınlık).

---

## 🔌 İletişim Stratejileri

- **Senkron İletişim:** Gateway -> Servisler arasında HTTP/REST, veya içeride bir servisin başka bir servise anlık ihtiyacı olursa **OpenFeign** istemcisi üzerinden yapılır.
- **Asenkron İletişim (Event-Driven):** `customerService`, `accountService`, `identityService` gibi servisler bir işlem (Örn: Müşteri Yaratıldı) yaptığında **Kafka**'ya bir mesaj (event) bırakır. `searchService` veya ilgili diğer servisler bu mesajı dinleyerek kendi veritabanlarını (örn. MongoDB) günceller veya tetiklenen bir iş sürecini başlatır.

---

## 🛠️ Kurulum ve Çalıştırma

### 1. Gereksinimler
- **Docker ve Docker Desktop**
- **Java 17 (JDK)**
- **Maven** (veya proje içindeki Maven Wrapper `mvnw`)

### 2. Projeyi Bilgisayarınıza İndirme
```bash
git clone <repository_url>
cd turkcell-crm
```

### 3. Altyapı ve Servisleri Docker İle Ayağa Kaldırma (Docker Compose)
Proje dizininde bulunan `docker-compose.yml` dosyası; Kafka, Zookeeper, Kafka UI, Redis, PostgreSQL veritabanları (accountdb, identitydbprod) ve temel Java servisleri için tanımlar barındırır.

```bash
# Tüm bağımlılıkları ve altyapı araçlarını arka planda başlatır
docker-compose up -d
```
*Not: Eğer imajlar hazır (build) değilse, Docker file'ları kullanarak kendi içinde imaj oluşturacaktır (build: ./klasor komutları).*

### 4. Lokal Geliştirme Ortamında Ayağa Kaldırma Sırası
Servisleri IDE (IntelliJ IDEA vb.) üzerinden lokal ortamınızda manuel çalıştıracaksanız, şu sırayı takip etmek ve önceki servisin tamamen başladığından emin olmak çok önemlidir:

1. **Altyapı Veritabanları ve Mesajlaşma:** (Önce `docker-compose up -d zookeeper kafka redis accountdb identitydbprod` çalıştırın).
2. **`configServer`** (Konfigürasyonları dağıtır, önce bu ayakta olmalı)
3. **`discoveryServer`** (Eureka Server ortamı yaratılır)
4. **`gatewayService`** (API yönetim kapısı)
5. **Diğer Business Servisleri:** (`identityService`, `accountService`, `searchService` vb. herhangi bir sırayla)

---

## 📚 API Dokümantasyonu

Projede Swagger / Springdoc WebMVC UI kütüphanesi entegre edilmiştir. Servisler ayağa kalktıktan sonra, ilgili servisin portu veya Gateway üzerinden şu yola gidilerek API dökümanına ve test arayüzüne ulaşılabilir:
- Lokal Servis İçin: `http://localhost:<SERVICE_PORT>/swagger-ui/index.html` VEYA `http://localhost:<SERVICE_PORT>/v3/api-docs`

---

## ⚙️ Ekstra Yapılandırma Notları

- **Ortak Paket:** Projede kod tekrarını önlemek için `io.github.yozyazici:common-package` bağımlılığı kullanılmaktadır (DTO, Core modüller vb.). Lokal kullanımda veya güncellemelerde o paketin sürüm uyumuna dikkat edilmelidir.
- **Kafka UI:** Docker üzerinden ayağa kalktığında `localhost:9090` (veya docker-compose'da belirtilen port) adresinden Kafka cluster'ını yönetebilirsiniz.

---
*Bu dokümantasyon, projenin root dizinindeki dosyalar (POM xml'ler ve docker-compose) baz alınarak otomatik mimari analiz sonucu üretilmiştir.*
