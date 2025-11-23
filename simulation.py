import requests
import random
from faker import Faker
from datetime import datetime, timedelta

# --- AYARLAR ---
BASE_URL = "http://localhost:8080/api"
FAKE = Faker('tr_TR') # Türkçe isimler ve veriler üretir

# ID Aralıkları (Veritabanındaki mevcut verilere göre)
MOVIE_ID_RANGE = (1, 20)   # 1 ile 20 arasındaki ID'li filmler
HALL_ID_RANGE = (1, 50)    # 1 ile 50 arasındaki ID'li salonlar

# =============================================================================
# 1. KULLANICI OLUŞTURMA
# =============================================================================
def create_users(count=100):
    print(f"\n👥 {count} Adet Rastgele Kullanıcı Oluşturuluyor...")
    created_user_ids = []

    for i in range(count):
        first_name = FAKE.first_name()
        last_name = FAKE.last_name()
        # E-posta çakışmasını önlemek için rastgele sayı ekle
        email = f"{first_name.lower()}.{last_name.lower()}.{random.randint(1000,9999)}@example.com"
        # Türkçe karakterleri temizle
        email = email.replace('ğ','g').replace('ü','u').replace('ş','s').replace('ı','i').replace('ö','o').replace('ç','c')

        payload = {
            "firstname": first_name,
            "lastname": last_name,
            "email": email,
            "password": "Password123!",
            "phoneNumber": FAKE.phone_number().replace(" ", "").replace("(", "").replace(")", ""),
            "birthday": FAKE.date_of_birth(minimum_age=18, maximum_age=70).strftime("%d-%m-%Y")
        }

        try:
            resp = requests.post(f"{BASE_URL}/register", json=payload)
            if resp.status_code == 201:
                user_data = resp.json()
                created_user_ids.append(user_data['id'])
        except Exception as e:
            pass # Hata olursa geç

    print(f"✨ Toplam {len(created_user_ids)} kullanıcı başarıyla oluşturuldu.")
    return created_user_ids

# =============================================================================
# 2. OTOMATİK SEANS (SHOWTIME) AÇMA
# =============================================================================
def create_showtimes(count=50):
    print(f"\n🎬 {count} Adet Rastgele Seans Oluşturuluyor...")
    created_showtime_ids = []

    for i in range(count):
        # --- DÜZELTİLEN KISIM BURASI ---
        # Gelecek 10 gün içine rastgele tarih
        random_days = random.randint(0, 10)
        date_obj = datetime.now() + timedelta(days=random_days)
        date_str = date_obj.strftime("%d-%m-%Y")

        # Rastgele saat (10:00 ile 23:00 arası)
        hour = random.randint(10, 23)
        minute = random.choice(["00", "15", "30", "45"])
        time_str = f"{hour}:{minute}"
        # -------------------------------

        payload = {
            "movieId": random.randint(*MOVIE_ID_RANGE),
            "hallId": random.randint(*HALL_ID_RANGE),
            "date": date_str,
            "startTime": time_str
        }

        try:
            resp = requests.post(f"{BASE_URL}/showtimes", json=payload)
            if resp.status_code == 201:
                show_data = resp.json()
                created_showtime_ids.append(show_data['id'])
        except Exception as e:
            pass

    print(f"✨ Toplam {len(created_showtime_ids)} seans oluşturuldu.")
    return created_showtime_ids

# =============================================================================
# 3. BİLET SATIN ALMA
# =============================================================================
def buy_tickets(user_ids, showtime_ids, ticket_count=200):
    print(f"\n🎟️  {ticket_count} Adet Bilet Satın Alınıyor...")
    success_count = 0

    for i in range(ticket_count):
        if not user_ids or not showtime_ids:
            break

        user_id = random.choice(user_ids)
        showtime_id = random.choice(showtime_ids)

        seat_letter = random.choice(['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'])
        seat_number = random.randint(1, 20)

        payload = {
            "userId": user_id,
            "showtimeId": showtime_id,
            "seatLetter": seat_letter,
            "seatNumber": seat_number,
            "price": random.choice([120.0, 150.0, 200.0, 250.0])
        }

        try:
            resp = requests.post(f"{BASE_URL}/tickets/buy", json=payload)
            if resp.status_code == 201:
                success_count += 1
        except:
            pass

    print(f"✨ Toplam {success_count} bilet başarıyla satıldı.")

# --- ANA ÇALIŞTIRMA ---
if __name__ == "__main__":
    users = create_users(100)
    if users:
        showtimes = create_showtimes(50)
        if showtimes:
            buy_tickets(users, showtimes, 300)

    print("\n🎉 SİMÜLASYON TAMAMLANDI!")