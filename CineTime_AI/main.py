from flask import Flask, jsonify
import pandas as pd
import psycopg2
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel

app = Flask(__name__)

# --- VERİTABANI AYARLARI ---
# Docker veya Local Postgres ayarlarınla aynı olmalı
DB_CONFIG = {
    'dbname': 'cinetime_db',
    'user': 'postgres',
    'password': '1234',  # Senin şifren
    'host': 'localhost',
    'port': '5432'
}

def get_db_connection():
    return psycopg2.connect(**DB_CONFIG)

# --- ÖNERİ MOTORU ---
def get_recommendations(user_id):
    conn = get_db_connection()

    # 1. Tüm Filmleri Çek
    movies_query = "SELECT id, title, summary, genre FROM movies"
    movies = pd.read_sql(movies_query, conn)

    # 2. Kullanıcının İzlediği Filmleri Çek (Biletlerden)
    user_history_query = f"""
        SELECT m.id, m.title 
        FROM tickets t 
        JOIN showtimes s ON t.showtime_id = s.id 
        JOIN movies m ON s.movie_id = m.id 
        WHERE t.user_id = {user_id}
    """
    user_movies = pd.read_sql(user_history_query, conn)

    conn.close()

    if user_movies.empty:
        return [] # Kullanıcı hiç film izlememişse öneri yok

    # 3. Yapay Zeka Hazırlığı (Metin İşleme)
    # Filmin özeti ve türünü birleştirip "karakteristiğini" çıkarıyoruz
    # NaN (Boş) değerleri temizle
    movies['summary'] = movies['summary'].fillna('')
    movies['genre'] = movies['genre'].fillna('')

    # Analiz edilecek metni oluştur: Özet + Tür
    movies['content'] = movies['summary'] + " " + movies['genre']

    # TF-IDF Matrisi (Kelimeleri sayılara dökme)
    tfidf = TfidfVectorizer(stop_words='english')
    tfidf_matrix = tfidf.fit_transform(movies['content'])

    # 4. Benzerlik Hesaplama (Cosine Similarity)
    # Her filmin diğer tüm filmlerle ne kadar benzediğini hesapla
    cosine_sim = linear_kernel(tfidf_matrix, tfidf_matrix)

    # 5. Kullanıcının izlediği filmlere benzerleri bul
    indices = pd.Series(movies.index, index=movies['id']).drop_duplicates()
    similar_scores = []

    # Kullanıcının izlediği her film için döngü
    for movie_id in user_movies['id']:
        if movie_id in indices:
            idx = indices[movie_id]
            # Bu filme benzeyenlerin skorlarını al
            sim_scores = list(enumerate(cosine_sim[idx]))
            similar_scores.extend(sim_scores)

    # 6. Sıralama ve Filtreleme
    # En yüksek benzerlik puanına göre sırala
    similar_scores = sorted(similar_scores, key=lambda x: x[1], reverse=True)

    # Zaten izlediklerini listeden çıkar
    watched_ids = user_movies['id'].tolist()
    recommendations = []
    seen_movies = set() # Aynı filmi tekrar önermemek için

    for i in similar_scores:
        movie_idx = i[0]
        recommended_id = movies.iloc[movie_idx]['id']

        # Eğer izlemediyse ve daha önce listeye eklemediysek
        if recommended_id not in watched_ids and recommended_id not in seen_movies:
            recommendations.append({
                "id": int(recommended_id),
                "title": movies.iloc[movie_idx]['title'],
                "genre": movies.iloc[movie_idx]['genre'],
                "similarity_score": round(i[1], 2) # Ne kadar benziyor? (0.0 - 1.0 arası)
            })
            seen_movies.add(recommended_id)

        if len(recommendations) >= 5: # En iyi 5 öneriyi ver
            break

    return recommendations

# --- API ENDPOINT ---
@app.route('/recommend/<int:user_id>', methods=['GET'])
def recommend(user_id):
    try:
        print(f"🤖 Kullanıcı {user_id} için öneriler hazırlanıyor...")
        recs = get_recommendations(user_id)

        if not recs:
            return jsonify({"message": "Bu kullanıcı için yeterli veri yok veya tüm filmleri izlemiş."}), 200

        return jsonify(recs)
    except Exception as e:
        print(f"❌ Hata: {e}")
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    print("🧠 CineTime AI Servisi Çalışıyor (Port 5001)...")
    # Mac'lerde 5000 portu dolu olduğu için 5001 yaptık
    app.run(port=5001)
