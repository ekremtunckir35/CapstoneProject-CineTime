import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

// Context Sağlayıcısı
import { AuthProvider } from "./context/AuthContext";

// Sayfalar
import HomePage from "./pages/HomePage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import MovieDetailPage from "./pages/MovieDetailPage.jsx";
import SeatSelectionPage from "./pages/SeatSelectionPage.jsx";
import ProfilePage from "./pages/ProfilePage.jsx"; // Yeni eklendi
import AdminPage from "./pages/AdminPage.jsx";     // Yeni eklendi

function App() {
    return (
        // 1. AuthProvider ile tüm uygulamayı sarmalıyoruz ki kullanıcı bilgisine her yerden erişelim
        <AuthProvider>
            <Router>
                {/* Bildirim Kutusu (Sağ altta çıkar) */}
                <ToastContainer position="bottom-right" theme="dark" />

                <Routes>
                    {/* 1. Ana Sayfa */}
                    <Route path="/" element={<HomePage />} />

                    {/* 2. Kimlik Doğrulama */}
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />

                    {/* 3. Film Detay Sayfası (ID parametresi alır) */}
                    <Route path="/movie/:id" element={<MovieDetailPage />} />

                    {/* 4. Bilet Alım / Koltuk Seçimi (Seans ID parametresi alır) */}
                    <Route path="/buy-ticket/:showtimeId" element={<SeatSelectionPage />} />

                    {/* 5. Kullanıcı Profil / Biletlerim Sayfası */}
                    <Route path="/profile" element={<ProfilePage />} />

                    {/* 6. Admin Paneli */}
                    <Route path="/admin" element={<AdminPage />} />
                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;