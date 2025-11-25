//parçaları birleştirdik.
// Sayfa açılınca hem vizyondaki hem de yakındaki filmleri çekeceğiz.

import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import MovieCard from "../components/MovieCard";
import movieService from "../services/movieService";

const HomePage = () => {
    const [inTheaters, setInTheaters] = useState([]);
    const [comingSoon, setComingSoon] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                // İki isteği aynı anda atıyoruz (Performans için Promise.all)
                const [theaterRes, comingRes] = await Promise.all([
                    movieService.getInTheaters(),
                    movieService.getComingSoon()
                ]);

                setInTheaters(theaterRes);
                setComingSoon(comingRes);
            } catch (error) {
                console.error("Filmler yüklenirken hata:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchMovies();
    }, []);

    return (
        <div className="bg-dark min-vh-100 text-white">
            <Navbar />

            {/* Hero Section (Tanıtım Alanı) */}
            <div className="container py-5 text-center">
                <h1 className="display-4 fw-bold text-primary">Sinemanın Büyüsü</h1>
                <p className="lead text-secondary">En yeni filmleri keşfet, koltuğunu seç ve anın tadını çıkar.</p>
            </div>

            <div className="container pb-5">
                {loading ? (
                    <div className="text-center py-5">
                        <div className="spinner-border text-primary" role="status"></div>
                        <p className="mt-2">Filmler Yükleniyor...</p>
                    </div>
                ) : (
                    <>
                        {/* Vizyondakiler Bölümü */}
                        <div className="d-flex justify-content-between align-items-center mb-4">
                            <h3 className="border-start border-4 border-primary ps-3">🔥 Vizyondakiler</h3>
                        </div>

                        <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4 mb-5">
                            {inTheaters.length > 0 ? (
                                inTheaters.map(movie => <MovieCard key={movie.id} movie={movie} />)
                            ) : (
                                <p className="text-muted">Şu an vizyonda film bulunamadı.</p>
                            )}
                        </div>

                        {/* Yakında Gelecekler Bölümü */}
                        <div className="d-flex justify-content-between align-items-center mb-4 mt-5">
                            <h3 className="border-start border-4 border-warning ps-3">📅 Yakında Gelecekler</h3>
                        </div>

                        <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
                            {comingSoon.length > 0 ? (
                                comingSoon.map(movie => <MovieCard key={movie.id} movie={movie} />)
                            ) : (
                                <p className="text-muted">Yakında gelecek film bulunamadı.</p>
                            )}
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default HomePage;