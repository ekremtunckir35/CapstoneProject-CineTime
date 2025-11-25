import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import ticketService from "../services/ticketService";
import recommendationService from "../services/recommendationService"; // <-- AI Servisi
import { useAuth } from "../context/AuthContext";
import MovieCard from "../components/MovieCard"; // <-- Film Kartları

const ProfilePage = () => {
    const { user } = useAuth();
    const [currentTickets, setCurrentTickets] = useState([]);
    const [pastTickets, setPastTickets] = useState([]);
    const [recommendations, setRecommendations] = useState([]); // <-- AI Önerileri State'i
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (user) {
            const fetchData = async () => {
                try {
                    // Biletleri ve Önerileri Paralel Çekiyoruz
                    const [current, past, recs] = await Promise.all([
                        ticketService.getCurrentTickets(user.id),
                        ticketService.getPastTickets(user.id),
                        recommendationService.getRecommendations(user.id) // AI Çağrısı
                    ]);

                    setCurrentTickets(current);
                    setPastTickets(past);
                    setRecommendations(recs);
                } catch (error) {
                    console.error("Veri çekme hatası:", error);
                } finally {
                    setLoading(false);
                }
            };
            fetchData();
        }
    }, [user]);

    // Bilet Kartı Bileşeni (Resim URL kontrolü eklendi)
    const TicketCard = ({ ticket, isPast }) => {
        const moviePoster = ticket.showtime.movie.poster;
        // Resim URL'si http ile başlıyorsa (dış link) olduğu gibi kullan, değilse backend'den al
        const imageUrl = moviePoster.startsWith("http")
            ? moviePoster
            : `http://localhost:8080/api/images/${moviePoster}`;

        return (
            <div className={`card mb-3 text-white ${isPast ? "bg-secondary opacity-75" : "bg-dark border-primary"}`}>
                <div className="row g-0">
                    <div className="col-md-2">
                        <img
                            src={imageUrl}
                            className="img-fluid rounded-start h-100"
                            style={{objectFit:"cover", width: "100%"}}
                            alt="poster"
                            onError={(e) => { e.target.src = "https://via.placeholder.com/150x225?text=No+Img"; }}
                        />
                    </div>
                    <div className="col-md-10">
                        <div className="card-body">
                            <h5 className="card-title text-primary">{ticket.showtime.movie.title}</h5>
                            <p className="card-text mb-1">
                                📅 <strong>Tarih:</strong> {ticket.showtime.date} | ⏰ {ticket.showtime.startTime}
                            </p>
                            <p className="card-text mb-1">
                                📍 <strong>Salon:</strong> {ticket.showtime.hall.name} | 💺 <strong>Koltuk:</strong> {ticket.seatLetter}{ticket.seatNumber}
                            </p>
                            <p className="card-text"><small className="text-white-50">Tutar: {ticket.price} TL</small></p>
                            {isPast && <span className="badge bg-secondary">İzlendi</span>}
                            {!isPast && <span className="badge bg-success">Aktif</span>}
                        </div>
                    </div>
                </div>
            </div>
        );
    };

    return (
        <div className="bg-dark min-vh-100 text-white">
            <Navbar />
            <div className="container py-5">
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h2>👤 Profilim: {user?.name}</h2>
                    {user?.role === "ADMIN" && <span className="badge bg-danger">Admin Yetkisi</span>}
                </div>

                <ul className="nav nav-tabs mb-4 border-secondary" id="myTab" role="tablist">
                    <li className="nav-item">
                        <button className="nav-link active text-white bg-dark border-secondary" data-bs-toggle="tab" data-bs-target="#current">
                            🎫 Aktif Biletlerim ({currentTickets.length})
                        </button>
                    </li>
                    <li className="nav-item">
                        <button className="nav-link text-white-50 bg-dark border-secondary" data-bs-toggle="tab" data-bs-target="#past">
                            📜 Geçmiş Biletlerim ({pastTickets.length})
                        </button>
                    </li>
                </ul>

                <div className="tab-content mb-5">
                    {/* Aktif Biletler */}
                    <div className="tab-pane fade show active" id="current">
                        {loading ? <p>Yükleniyor...</p> : (
                            currentTickets.length > 0 ?
                                currentTickets.map(t => <TicketCard key={t.ticketId} ticket={t} isPast={false} />) :
                                <p className="text-muted">Henüz aktif bir biletiniz yok.</p>
                        )}
                    </div>

                    {/* Geçmiş Biletler */}
                    <div className="tab-pane fade" id="past">
                        {pastTickets.length > 0 ?
                            pastTickets.map(t => <TicketCard key={t.ticketId} ticket={t} isPast={true} />) :
                            <p className="text-muted">Geçmiş bilet kaydı bulunamadı.</p>
                        }
                    </div>
                </div>

                {/* --- YAPAY ZEKA ÖNERİLERİ BÖLÜMÜ --- */}
                {recommendations.length > 0 && (
                    <div className="mt-5 pt-4 border-top border-secondary">
                        <h3 className="mb-4 text-warning">✨ Yapay Zeka Senin İçin Seçti</h3>
                        <p className="text-muted mb-4">
                            Geçmiş biletlerine ve izleme alışkanlıklarına göre bu filmleri seveceğini düşünüyoruz.
                        </p>
                        <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-4">
                            {recommendations.map(movie => (
                                <MovieCard key={movie.id} movie={movie} />
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ProfilePage;