import requests
import random

# --- AYARLAR ---
BASE_URL = "http://localhost:8080/api"

# --- GERÇEKÇİ ŞEHİR VE AVM VERİLERİ (20 Büyükşehir) ---
CITY_DATA = {
    "İstanbul": ["Cevahir AVM", "Marmara Forum", "Mall of Istanbul", "Zorlu Center", "Akasya AVM", "Kanyon", "Vadistanbul"],
    "Ankara": ["ANKAmall", "Panora AVM", "Armada", "Kentpark", "CEPA AVM", "Gordion"],
    "İzmir": ["İstinyePark İzmir", "İzmir Optimum", "Forum Bornova", "Agora AVM", "Hilltown Karşıyaka"],
    "Bursa": ["Korupark", "Sur Yapı Marka", "Zafer Plaza", "Kent Meydanı AVM"],
    "Antalya": ["TerraCity", "Migros AVM", "MarkAntalya", "Mall of Antalya"],
    "Adana": ["M1 Adana", "Optimum AVM", "Esas 01 Burda"],
    "Konya": ["Kentplaza", "M1 Konya", "Kulesite"],
    "Gaziantep": ["Sanko Park", "Forum Gaziantep", "Primemall"],
    "Şanlıurfa": ["Urfa City AVM", "Piazza AVM"],
    "Kocaeli": ["41 Burda", "Symbol AVM", "Gebze Center"],
    "Mersin": ["Forum Mersin", "Sayapark AVM", "Marina AVM"],
    "Diyarbakır": ["Ceylan Karavil Park", "Forum Diyarbakır"],
    "Hatay": ["Palladium Antakya", "Primemall Antakya"],
    "Manisa": ["Manisa Prime", "Magnesia AVM"],
    "Kayseri": ["Kayseri Park", "Forum Kayseri"],
    "Samsun": ["Yeşilyurt AVM", "Piazza Samsun", "Bulvar AVM"],
    "Balıkesir": ["10 Burda", "Yaylada AVM"],
    "Kahramanmaraş": ["Piazza Kahramanmaraş"],
    "Van": ["Van AVM"],
    "Aydın": ["Forum Aydın", "Aydın AVM"],
    "Eskişehir": ["Espark", "Vega Outlet", "Özdilek"], # Bonus :)
    "Trabzon": ["Forum Trabzon", "Varlıbaş AVM"]      # Bonus :)
}

# Salon İsim Havuzu
HALL_NAMES = ["Salon 1", "Salon 2", "Salon 3", "Salon 4", "IMAX Salonu", "Gold Class", "4DX Salon", "VIP Salon", "Junior Salon"]

def seed_places():
    print(f"\n🏙️  Türkiye Geneli Sinema Veritabanı Oluşturuluyor... ({len(CITY_DATA)} Şehir Hedeflendi)")

    total_cinemas = 0
    total_halls = 0

    for city_name, malls in CITY_DATA.items():
        # 1. ŞEHİR EKLE
        print(f"\n📍 Şehir Ekleniyor: {city_name}...")
        try:
            city_resp = requests.post(f"{BASE_URL}/cinemas/city", json={"name": city_name})

            if city_resp.status_code == 201:
                city_data = city_resp.json()
                city_id = city_data['id']

                # Bu şehirdeki AVM listesinden sinemalar oluştur
                # (Her AVM'yi eklemiyoruz, rastgele 2-3 tanesini seçiyoruz ki çok şişmesin, istersen hepsini ekle)
                selected_malls = malls if len(malls) < 3 else random.sample(malls, 3)

                for mall_name in selected_malls:
                    cinema_name = f"CineTime {mall_name}"
                    cinema_payload = {
                        "name": cinema_name,
                        "address": f"{mall_name}, {city_name}",
                        "cityId": city_id
                    }

                    # 2. SİNEMA EKLE
                    cinema_resp = requests.post(f"{BASE_URL}/cinemas", json=cinema_payload)
                    if cinema_resp.status_code == 201:
                        cinema_data = cinema_resp.json()
                        cinema_id = cinema_data['id']
                        total_cinemas += 1
                        print(f"  🏢 Sinema: {cinema_name}")

                        # Her sinemaya rastgele 3-5 Salon ekle
                        num_halls = random.randint(3, 5)
                        selected_halls = random.sample(HALL_NAMES, num_halls)

                        for hall_name in selected_halls:
                            is_special = "IMAX" in hall_name or "4DX" in hall_name or "Gold" in hall_name
                            hall_payload = {
                                "name": hall_name,
                                "seatCapacity": random.choice([80, 120, 150, 200, 250]),
                                "isSpecial": is_special,
                                "cinemaId": cinema_id
                            }

                            # 3. SALON EKLE
                            hall_resp = requests.post(f"{BASE_URL}/cinemas/hall", json=hall_payload)
                            if hall_resp.status_code == 201:
                                total_halls += 1

            else:
                print(f"⚠️  Şehir zaten var veya eklenemedi: {city_name}")

        except Exception as e:
            print(f"❌ Bağlantı Hatası: {e}")

    print(f"\n✅ İŞLEM TAMAMLANDI!")
    print(f"📊 Toplam Eklenen Şehir: {len(CITY_DATA)}")
    print(f"📊 Toplam Eklenen Sinema: {total_cinemas}")
    print(f"📊 Toplam Eklenen Salon: {total_halls}")

if __name__ == "__main__":
    seed_places()