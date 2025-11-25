import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api"; // Axios servisimiz
import { toast } from "react-toastify";

const RegisterPage = () => {
    const navigate = useNavigate();

    // Form verilerini tutan state
    const [formData, setFormData] = useState({
        firstname: "",
        lastname: "",
        email: "",
        password: "",
        phoneNumber: "",
        birthDate: "" // YYYY-MM-DD formatında göndermeye çalışacağız
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Basit Validasyon
        if (!formData.firstname || !formData.lastname || !formData.email || !formData.password) {
            toast.warning("Lütfen zorunlu alanları doldurun.");
            return;
        }

        try {
            // Tarih formatını Backend'in istediği formatta (dd-MM-yyyy) gönderelim
            // HTML input type="date" bize YYYY-MM-DD verir (Örn: 2023-11-25)
            // Bunu 25-11-2023 yapmalıyız.
            const dateParts = formData.birthDate.split("-");
            const formattedDate = `${dateParts[2]}-${dateParts[1]}-${dateParts[0]}`;

            const payload = {
                ...formData,
                birthDate: formattedDate
            };

            // Backend'e istek at (PDF U02)
            const response = await api.post("/register", payload);

            if (response.status === 201 || response.status === 200) {
                toast.success("Kayıt başarılı! Şimdi giriş yapabilirsiniz.");
                navigate("/login"); // Login sayfasına yönlendir
            }
        } catch (error) {
            console.error("Register Error:", error);
            // Backend'den gelen özel hata mesajı varsa onu göster, yoksa genel hata
            const errorMsg = error.response?.data?.message || "Kayıt olurken bir hata oluştu.";
            toast.error(errorMsg);
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center min-vh-100 py-5">
            <div className="card bg-dark text-white p-4 shadow-lg" style={{ width: "500px", borderRadius: "15px" }}>
                <div className="card-body">
                    <h2 className="text-center mb-4 text-primary fw-bold">CineTime</h2>
                    <h5 className="text-center mb-4">Yeni Hesap Oluştur</h5>

                    <form onSubmit={handleSubmit}>
                        <div className="row">
                            {/* Ad */}
                            <div className="col-md-6 mb-3">
                                <label className="form-label">Ad</label>
                                <input type="text" name="firstname" className="form-control bg-secondary text-white border-0"
                                       value={formData.firstname} onChange={handleChange} required />
                            </div>
                            {/* Soyad */}
                            <div className="col-md-6 mb-3">
                                <label className="form-label">Soyad</label>
                                <input type="text" name="lastname" className="form-control bg-secondary text-white border-0"
                                       value={formData.lastname} onChange={handleChange} required />
                            </div>
                        </div>

                        {/* Email */}
                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input type="email" name="email" className="form-control bg-secondary text-white border-0"
                                   value={formData.email} onChange={handleChange} required />
                        </div>

                        {/* Şifre */}
                        <div className="mb-3">
                            <label className="form-label">Şifre</label>
                            <input type="password" name="password" className="form-control bg-secondary text-white border-0"
                                   value={formData.password} onChange={handleChange} required />
                        </div>

                        {/* Telefon */}
                        <div className="mb-3">
                            <label className="form-label">Telefon (5551234567)</label>
                            <input type="text" name="phoneNumber" className="form-control bg-secondary text-white border-0"
                                   value={formData.phoneNumber} onChange={handleChange} required />
                        </div>

                        {/* Doğum Tarihi */}
                        <div className="mb-3">
                            <label className="form-label">Doğum Tarihi</label>
                            <input type="date" name="birthDate" className="form-control bg-secondary text-white border-0"
                                   value={formData.birthDate} onChange={handleChange} required />
                        </div>

                        <button type="submit" className="btn btn-primary w-100 mt-3 fw-bold">
                            Kayıt Ol
                        </button>
                    </form>

                    <div className="text-center mt-3">
                        <small>Zaten hesabın var mı? <Link to="/login" className="text-decoration-none text-info">Giriş Yap</Link></small>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;