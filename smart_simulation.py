import requests
import random
from datetime import datetime, timedelta

# --- AYARLAR ---
BASE_URL = "http://localhost:8080/api"

def get_all_movie_ids():
    print("🔍 Filmler taranıyor...")
    try:
        # Sayfalama olduğu için 'content' içinden alıyoruz
        resp = requests.get(f"{BASE_URL}/movies?size=1000")
        if resp.status_code == 200:
            data = resp.json()
            movies = data.get('content', [])
            return [m['id'] for m in movies]
    except Exception as e:
        print(f"⚠️ Film verisi çekilemedi: {e}")
    return []

def get_all_hall_ids():
    print("🔍 Salonlar taranıyor...")
    hall_ids = []
    try:
        # 1. Sinemaları Çek
        resp = requests.get(f"{BASE_URL}/cinemas")
        if resp.status_code == 200:
            cinemas = resp.json()
            # 2. Her Sinemanın Salonlarını Çek
            for cinema in cinemas:
                h_resp = requests.get(f"{BASE_URL}/cinemas/{cinema['id']}/halls")
                if h_resp.status_code == 200:
                    halls = h_resp.json()
                    hall_ids.extend([h['id'] for h in halls])
    except Exception as e:
        print(f"⚠️ Salon verisi çekilemedi: {e}")
    return hall_ids

def create_smart_showtimes(count=50):
    # 1. Gerçek ID'leri Bul
    movie_ids = get_all_movie_ids()
    hall_ids = get_all_hall_ids()

    print(f"📊 Durum: {len(movie_ids)} Film ve {len(hall_ids)} Salon bulundu.")

    if not movie_ids:
        print("❌ HATA: Hiç film bulunamadı! Önce 'python3 seeder.py' çalıştırın.")
        return
    if not hall_ids:
        print("❌ HATA: Hiç salon bulunamadı! Önce 'python3 places_seeder.py' çalıştırın.")
        return

    print(f"\n🚀 {count} Adet Akıllı Seans Oluşturuluyor...")

    success_count = 0
    for i in range(count):
        # Rastgele Tarih (Gelecek 7 gün)
        random_days = random.randint(0, 7)
        date_obj = datetime.now() + timedelta(days=random_days)
        date_str = date_obj.strftime("%d-%m-%Y")

        # Rastgele Saat
        hour = random.randint(10, 22)
        minute = random.choice(["00", "15", "30", "45"])
        time_str = f"{hour}:{minute}"

        payload = {
            "movieId": random.choice(movie_ids), # <-- GERÇEK ID KULLANIYORUZ
            "hallId": random.choice(hall_ids),   # <-- GERÇEK ID KULLANIYORUZ
            "date": date_str,
            "startTime": time_str
        }

        try:
            resp = requests.post(f"{BASE_URL}/showtimes", json=payload)
            if resp.status_code == 201:
                success_count += 1
                print(f"  ✅ Seans Eklendi: {date_str} {time_str}")
        except Exception as e:
            print(f"  ❌ Hata: {e}")

    print(f"\n✨ İşlem Tamam! Toplam {success_count} seans başarıyla oluşturuldu.")

if __name__ == "__main__":
    create_smart_showtimes(50)