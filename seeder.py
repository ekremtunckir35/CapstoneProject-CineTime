import requests
import json
import os

# --- AYARLAR ---
TMDB_API_KEY = "51ec09416d77ea5823af5bcd3971015e" # <--- BURAYI DEGISTIR!
JAVA_BACKEND_URL = "http://localhost:8080/api"
TMDB_BASE_URL = "https://api.themoviedb.org/3"
IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

# --- 1. FİLMLERİ ÇEK (TMDB) ---
def get_now_playing_movies():
    url = f"{TMDB_BASE_URL}/movie/now_playing?api_key={TMDB_API_KEY}&language=tr-TR&page=1"
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()['results']
    else:
        print("Hata: TMDB'den veri çekilemedi!")
        return []

# --- 2. FİLM DETAYLARINI ÇEK (Süre, Oyuncular vb. için) ---
def get_movie_details(movie_id):
    url = f"{TMDB_BASE_URL}/movie/{movie_id}?api_key={TMDB_API_KEY}&language=tr-TR&append_to_response=credits"
    response = requests.get(url)
    return response.json()

# --- 3. RESMİ İNDİR VE JAVA'YA YÜKLE ---
def upload_image_to_java(poster_path):
    if not poster_path:
        return "default.jpg"
    
    image_url = f"{IMAGE_BASE_URL}{poster_path}"
    image_name = poster_path[1:] # Baştaki / işaretini kaldır
    
    # Resmi indir
    img_response = requests.get(image_url)
    if img_response.status_code == 200:
        # Geçici olarak kaydet
        with open(image_name, 'wb') as f:
            f.write(img_response.content)
        
        # Java Backend'e Yükle
        files = {'image': (image_name, open(image_name, 'rb'), 'image/jpeg')}
        try:
            java_response = requests.post(f"{JAVA_BACKEND_URL}/images", files=files)
            print(f"Resim Yüklendi: {image_name} -> {java_response.status_code}")
            return image_name
        except Exception as e:
            print(f"Resim yükleme hatası: {e}")
            return "default.jpg"
        finally:
            # Temizlik: İndirilen dosyayı sil
            if os.path.exists(image_name):
                os.remove(image_name)
    return "default.jpg"

# --- ANA İŞLEM ---
def seed_database():
    print("🎬 CineTime Veri Yükleyici Başlıyor...")
    movies = get_now_playing_movies()
    
    for m in movies:
        details = get_movie_details(m['id'])
        
        # 1. Resmi Yükle
        poster_name = upload_image_to_java(m['poster_path'])
        
        # 2. Oyuncuları Al (İlk 3 kişi)
        cast_list = [actor['name'] for actor in details['credits']['cast'][:3]]
        
        # 3. Türleri Al (İlk tür)
        genre = details['genres'][0]['name'] if details['genres'] else "Genel"

        # 4. Java İçin JSON Hazırla
        movie_data = {
            "title": m['title'],
            "summary": m['overview'] if m['overview'] else "Özet bulunamadı.",
            "releaseDate": m['release_date'].split("-")[2] + "-" + m['release_date'].split("-")[1] + "-" + m['release_date'].split("-")[0], # YYYY-MM-DD -> dd-MM-yyyy Çevirimi
            "duration": details['runtime'] if details['runtime'] else 90,
            "director": "Bilinmiyor", # TMDB'den yönetmeni çekmek ekstra döngü gerektirir, şimdilik sabit
            "cast": cast_list,
            "formats": ["2D", "Subtitled"],
            "genre": genre,
            "poster": poster_name,
            "status": "IN_THEATERS"
        }

        # 5. Filmi Java'ya Kaydet
        try:
            res = requests.post(f"{JAVA_BACKEND_URL}/movies", json=movie_data)
            if res.status_code == 201:
                print(f"✅ Eklendi: {m['title']}")
            else:
                print(f"❌ Eklenemedi: {m['title']} - {res.text}")
        except Exception as e:
            print(f"Bağlantı Hatası: {e}")

if __name__ == "__main__":
    seed_database()
