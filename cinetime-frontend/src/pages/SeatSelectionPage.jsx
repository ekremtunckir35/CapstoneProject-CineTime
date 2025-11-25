import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";
import { toast } from "react-toastify";

const SeatSelectionPage = () => {
    const { showtimeId } = useParams(); // URL'den seans ID'sini al
    const navigate = useNavigate();

    const [loading, setLoading] = useState(true);
    const [showtime, setShowtime] = useState(null); // Seans bilgisi
    const [takenSeats, setTakenSeats] = useState([]); // Dolu koltuklar
    const [selectedSeats, setSelectedSeats] = useState([]); // Seçilenler

    const TICKET_PRICE = 150; // Sabit fiyat

    // Koltuk Düzeni (A-G sıraları, her sırada 8 koltuk)
    const rows = ["A", "B", "C", "D", "E", "F", "G"];
    const seatsPerRow = 8;

    useEffect(() => {
        const fetchDetails = async () => {
            try {
                // 1. Seans detaylarını bul
                const response = await api.get("/showtimes");
                const currentShow = response.data.find(s => s.id === parseInt(showtimeId));

                if (currentShow) {
                    setShowtime(currentShow);
                    // 2. Simülasyon: Rastgele dolu koltuklar
                    const mockTaken = ["A1", "A2", "C4", "D5", "E8"];
                    setTakenSeats(mockTaken);
                } else {
                    toast.error("Seans bulunamadı!");
                    navigate("/");
                }
            } catch (error) {
                console.error("Hata:", error);
                toast.error("Veriler yüklenirken hata oluştu.");
            } finally {
                setLoading(false);
            }
        };

        fetchDetails();
    }, [showtimeId, navigate]);

    // Koltuk Seçim Mantığı
    const toggleSeat = (seatId) => {
        if (takenSeats.includes(seatId)) return; // Doluysa işlem yapma

        if (selectedSeats.includes(seatId)) {
            setSelectedSeats(selectedSeats.filter(id => id !== seatId)); // Seçimi kaldır
        } else {
            setSelectedSeats([...selectedSeats, seatId]); // Seçime ekle
        }
    };

    // Satın Alma İşlemi
    const handleBuy = async () => {
        if (selectedSeats.length === 0) {
            toast.warning("Lütfen en az bir koltuk seçin.");
            return;
        }

        // LocalStorage'dan kullanıcıyı çek
        const user = JSON.parse(localStorage.getItem("user"));
        const userId = user ? user.id : null;

        // Eğer kullanıcı giriş yapmamışsa Login'e yönlendir
        if (!userId) {
            toast.error("Bilet almak için lütfen önce giriş yapın!");
            navigate("/login");
            return;
        }

        try {
            // Her koltuk için tek tek istek at (PDF T04)
            for (const seat of selectedSeats) {
                const row = seat.charAt(0);
                const number = parseInt(seat.substring(1));

                const payload = {
                    userId: userId,
                    showtimeId: parseInt(showtimeId),
                    seatLetter: row,
                    seatNumber: number,
                    price: TICKET_PRICE
                };

                await api.post("/tickets/buy", payload);
            }

            toast.success("🎉 Biletleriniz başarıyla oluşturuldu!");
            navigate("/"); // Ana sayfaya dön
        } catch (error) {
            console.error(error);
            const msg = error.response?.data?.message || "Satın alma başarısız.";
            toast.error(msg);
        }
    };

    if (loading) return <div className="text-center text-white mt-5">Yükleniyor...</div>;

    return (
        <div className="bg-dark min-vh-100 text-white p-4 d-flex flex-column align-items-center">

            {/* Üst Bilgi */}
            <div className="text-center mb-4">
                <h2 className="text-primary fw-bold">{showtime?.movie?.title}</h2>
                <p className="text-secondary fs-5">
                    {showtime?.date} | {showtime?.startTime?.substring(0,5)} | {showtime?.hall?.name}
                </p>
            </div>

            {/* PERDE (Görsel Efekt) */}
            <div className="mb-5 text-center text-dark fw-bold py-2 shadow-lg"
                 style={{
                     width: "80%",
                     maxWidth: "600px",
                     background: "linear-gradient(to bottom, #e50914, #800000)",
                     borderRadius: "0 0 50px 50px",
                     boxShadow: "0 10px 30px rgba(229, 9, 20, 0.3)",
                     transform: "perspective(300px) rotateX(-2deg)"
                 }}>
                SİNEMA PERDESİ
            </div>

            {/* KOLTUK MATRİSİ */}
            <div className="d-flex flex-column gap-2 mb-5">
                {rows.map(row => (
                    <div key={row} className="d-flex gap-2 justify-content-center">
                        {Array.from({ length: seatsPerRow }, (_, i) => i + 1).map(num => {
                            const seatId = `${row}${num}`;
                            const isTaken = takenSeats.includes(seatId);
                            const isSelected = selectedSeats.includes(seatId);

                            return (
                                <div
                                    key={seatId}
                                    onClick={() => toggleSeat(seatId)}
                                    className={`
                                      d-flex justify-content-center align-items-center rounded shadow-sm
                                      ${isTaken ? "bg-secondary opacity-50" : isSelected ? "bg-success text-white" : "bg-light text-dark"}
                                    `}
                                    style={{
                                        width: "40px", height: "40px",
                                        cursor: isTaken ? "not-allowed" : "pointer",
                                        transition: "all 0.2s"
                                    }}
                                    title={isTaken ? "Dolu" : `Koltuk ${seatId}`}
                                >
                                    <small className="fw-bold" style={{fontSize: "0.75rem"}}>{seatId}</small>
                                </div>
                            );
                        })}
                    </div>
                ))}
            </div>

            {/* Alt Panel (Özet ve Satın Al) */}
            <div className="card bg-secondary text-white border-0 shadow-lg w-100" style={{maxWidth: "600px"}}>
                <div className="card-body d-flex justify-content-between align-items-center">
                    <div>
                        <div className="mb-1">Seçilen: <span className="fw-bold text-warning">{selectedSeats.length > 0 ? selectedSeats.join(", ") : "-"}</span></div>
                        <div className="fs-5">Toplam: <span className="fw-bold">{selectedSeats.length * TICKET_PRICE} TL</span></div>
                    </div>
                    <button
                        onClick={handleBuy}
                        disabled={selectedSeats.length === 0}
                        className="btn btn-primary px-4 py-2 fw-bold shadow"
                    >
                        Bilet Al 🎟️
                    </button>
                </div>
            </div>

        </div>
    );
};

export default SeatSelectionPage;