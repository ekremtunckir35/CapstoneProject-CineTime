# 🍿 CineTime - AI Destekli Sinema Biletleme Sistemi

**CineTime**, kullanıcıların vizyondaki filmleri inceleyebildiği, koltuk seçerek bilet alabildiği ve geçmiş izleme alışkanlıklarına göre **Yapay Zeka (AI)** destekli film önerileri alabildiği kapsamlı bir Full-Stack web uygulamasıdır.

![CineTime Banner](https://via.placeholder.com/1000x400?text=CineTime+Project+Showcase)
*(Buraya projenin ana ekran görüntüsünü ekleyebilirsiniz)*

## 🚀 Özellikler

* **🔐 Kimlik Doğrulama:** JWT (JSON Web Token) tabanlı güvenli giriş ve kayıt sistemi.
* **🎬 Film Keşfi:** Vizyondaki ve yakında gelecek filmleri listeleme.
* **💺 Koltuk Seçimi:** Dinamik ve görsel salon/koltuk seçim ekranı.
* **🎫 Biletleme:** Gerçek zamanlı bilet satın alma ve veritabanına kayıt.
* **🧠 Yapay Zeka Önerileri:** Kullanıcının izlediği filmlere göre (Content-Based Filtering) kişiselleştirilmiş film önerileri sunan Python Mikroservisi.
* **👤 Profil Yönetimi:** Geçmiş ve gelecek biletlerin listelendiği kullanıcı paneli.
* **👑 Admin Paneli:** Film ekleme ve silme işlemleri.
* **🤖 Otomasyon:** TMDB API'den film çekme ve rastgele seans oluşturma scriptleri.

## 🛠️ Teknolojiler

Bu proje modern ve güçlü bir teknoloji yığını ile geliştirilmiştir:

### Backend (Java & Spring Boot)
* **Java 17**
* **Spring Boot 3.1.4** (Web, Data JPA, Security, Validation)
* **PostgreSQL** (İlişkisel Veritabanı)
* **JWT (jjwt)** (Token tabanlı güvenlik)
* **Lombok**

### Frontend (React)
* **React.js (Vite)**
* **Bootstrap 5 & SCSS** (Responsive Tasarım)
* **Axios** (API İstekleri)
* **React Router DOM** (Sayfa Yönlendirme)
* **React Toastify** (Bildirimler)

### Yapay Zeka (Python)
* **Python 3.9+**
* **Flask** (API Servisi)
* **Pandas & Scikit-learn** (Veri İşleme ve TF-IDF Algoritması)
* **Psycopg2** (Veritabanı Bağlantısı)

---

## ⚙️ Kurulum ve Çalıştırma

Projeyi yerel makinenizde çalıştırmak için aşağıdaki adımları takip edin.

### Ön Hazırlık
* Bilgisayarınızda **Java 17**, **Node.js**, **Python 3** ve **PostgreSQL** yüklü olmalıdır.
* PostgreSQL'de `cinetime_db` adında boş bir veritabanı oluşturun.

### 1. Backend (Java) Kurulumu
1.  `application.properties` dosyasındaki veritabanı kullanıcı adı ve şifresini kendi ayarlarınıza göre düzenleyin.
2.  Projeyi terminalde açın ve başlatın:
    ```bash
    ./mvnw spring-boot:run
    ```
    *(Backend `8080` portunda çalışacaktır).*

### 2. Veri Doldurma (Seeding - İsteğe Bağlı)
Uygulama ilk açıldığında veritabanı boş olabilir. Python scriptleri ile otomatik doldurabilirsiniz:
*(Önce `pip install requests faker` yapın)*

```bash
# 1. TMDB'den Filmleri Çek
python3 seeder.py

# 2. Şehir ve Sinemaları Oluştur
python3 places_seeder.py

# 3. Rastgele Seanslar Oluştur
python3 smart_simulation.py