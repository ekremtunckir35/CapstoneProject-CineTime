//Bu sayfa, kullanıcının filmi seçip "Bilet Al" diyeceği kritik nokta.

import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import movieService from "../services/movieService";
import showtimeService from "../services/showtimeService";
import { toast } from "react-toastify";

const MovieDetailPage = () => {
    const { id } = useParams(); // URL'deki ID'yi al (örn: /movie/5 -> id=5)
    const [movie, setMovie] = useState(null);
    const [showtimes, setShowtimes] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                // İki isteği paralel atıyoruz (Film Detayı + Seanslar)
                const [movieRes, showtimeRes] = await Promise.all([
                    movieService.getMovieById(id),
                    showtimeService.getShowtimesByMovie(id)
                ]);

                setMovie(movieRes);
                setShowtimes(showtimeRes);
            } catch (error) {
                console.error("Detay hatası:", error);
                toast.error("Film bilgileri yüklenemedi.");
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [id]);

    if (loading) return <div className="text-center text-white mt-5">Yükleniyor...</div>;
    if (!movie) return <div className="text-center text-white mt-5">Film bulunamadı!</div>;

    return (
        <div className="bg-dark min-vh-100 text-white">
            <Navbar />

            {/* Arka Plan Bulanıklığı (Opsiyonel Estetik) */}
            <div
                style={{
                    backgroundImage: `url(${movie.poster})`,
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    opacity: 0.1,
                    position: "absolute",
                    top: 0, left: 0, right: 0, bottom: 0,
                    zIndex: 0
                }}
            ></div>

            <div className="container py-5 position-relative" style={{ zIndex: 1 }}>
                <div className="row">
                    {/* SOL: Poster */}
                    <div className="col-md-4 mb-4">
                        <img
                            src={movie.poster && movie.poster.startsWith("http")
                                ? movie.poster
                                : `http://localhost:8080/api/images/${movie.poster}`}

                            alt={movie.title}
                            className="img-fluid rounded shadow-lg border border-secondary"
                            style={{ maxHeight: "600px", width: "100%", objectFit: "cover" }}
                        />
                    </div>

                    {/* SAĞ: Bilgiler */}
                    <div className="col-md-8">
                        <h1 className="display-4 fw-bold text-primary">{movie.title}</h1>
                        <div className="d-flex gap-3 text-secondary mb-3 align-items-center">
                            <span className="badge bg-warning text-dark fs-6">{movie.genre}</span>
                            <span>⏱ {movie.duration} dakika</span>
                            <span>📅 {movie.releaseDate}</span>
                        </div>

                        <p className="lead text-light mb-4">{movie.summary}</p>

                        {/* Oyuncular */}
                        <div className="mb-4">
                            <h5 className="text-info">Oyuncular</h5>
                            <p className="text-secondary">
                                {movie.cast && movie.cast.join(", ")}
                            </p>
                        </div>

                        <hr className="border-secondary" />

                        {/* SEANSLAR BÖLÜMÜ */}
                        <h3 className="mb-3">📅 Seanslar ve Bilet Al</h3>

                        {showtimes.length === 0 ? (
                            <div className="alert alert-warning bg-opacity-10 border-warning text-warning">
                                Bu film için şu an planlanmış bir seans bulunmamaktadır.
                            </div>
                        ) : (
                            <div className="d-flex flex-wrap gap-2">
                                {showtimes.map((show) => (
                                    <Link
                                        key={show.id}
                                        to={`/buy-ticket/${show.id}`} // Bilet alma sayfasına gidecek ID
                                        className="btn btn-outline-light d-flex flex-column align-items-center p-2 px-3"
                                    >
                                        <span className="small text-secondary">{show.date}</span>
                                        <span className="fw-bold fs-5">{show.startTime.substring(0, 5)}</span>
                                        <span className="badge bg-secondary mt-1" style={{fontSize: "0.7rem"}}>
                        {show.hall?.name || "Salon X"}
                    </span>
                                    </Link>
                                ))}
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MovieDetailPage;