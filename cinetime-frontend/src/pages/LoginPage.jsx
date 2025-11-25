import { useState } from "react";
import { useAuth } from "../context/AuthContext"; // Context'ten login fonksiyonunu alıyoruz
import { useNavigate, Link } from "react-router-dom"; // Sayfa yönlendirmesi için
import { toast } from "react-toastify"; // Hata mesajı için (Opsiyonel, Context'te de var)

const LoginPage = () => {
    const { login } = useAuth();
    const navigate = useNavigate();

    // Form verilerini tutan state
    const [formData, setFormData] = useState({
        email: "",
        password: "",
    });

    // Kullanıcı inputlara bir şey yazdığında state'i günceller
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Form gönderildiğinde çalışır
    const handleSubmit = async (e) => {
        e.preventDefault(); // Sayfanın yenilenmesini engelle

        // Validasyon (Basit kontrol)
        if (!formData.email || !formData.password) {
            toast.warning("Lütfen tüm alanları doldurun.");
            return;
        }

        // Context'teki login fonksiyonunu çağır
        const success = await login(formData);

        // Eğer giriş başarılıysa Ana Sayfaya yönlendir
        if (success) {
            navigate("/");
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card bg-dark text-white p-4 shadow-lg" style={{ width: "400px", borderRadius: "15px" }}>
                <div className="card-body">
                    <h2 className="text-center mb-4 text-primary fw-bold">CineTime</h2>
                    <h5 className="text-center mb-4">Giriş Yap</h5>

                    <form onSubmit={handleSubmit}>
                        {/* Email Input */}
                        <div className="mb-3">
                            <label className="form-label">Email Adresi</label>
                            <input
                                type="email"
                                name="email"
                                className="form-control bg-secondary text-white border-0"
                                placeholder="ornek@email.com"
                                value={formData.email}
                                onChange={handleChange}
                            />
                        </div>

                        {/* Password Input */}
                        <div className="mb-3">
                            <label className="form-label">Şifre</label>
                            <input
                                type="password"
                                name="password"
                                className="form-control bg-secondary text-white border-0"
                                placeholder="******"
                                value={formData.password}
                                onChange={handleChange}
                            />
                        </div>

                        {/* Giriş Butonu */}
                        <button type="submit" className="btn btn-primary w-100 mt-3 fw-bold">
                            Giriş Yap
                        </button>
                    </form>

                    {/* Kayıt Ol Linki */}
                    <div className="text-center mt-3">
                        <small>Hesabın yok mu? <Link to="/register" className="text-decoration-none text-info">Kayıt Ol</Link></small>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;