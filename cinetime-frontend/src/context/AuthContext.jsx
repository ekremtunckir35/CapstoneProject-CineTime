import { createContext, useState, useContext, useEffect } from "react";
import api from "../services/api"; // api servisimizin importu
import { toast } from "react-toastify"; // Hata mesajları için

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true); // Sayfa yenilendiğinde kontrol için

    // 1. Uygulama açıldığında: Kullanıcı daha önce giriş yapmış mı?
    useEffect(() => {
        const checkUserLoggedIn = async () => {
            try {
                const token = localStorage.getItem("token");
                if (token) {
                    // Eğer token varsa, localStorage'daki kullanıcı bilgisini alıp state'e yüklüyoruz
                    const storedUser = localStorage.getItem("user");
                    if (storedUser) {
                        setUser(JSON.parse(storedUser));
                    }
                }
            } catch (error) {
                console.error("Giriş kontrol hatası:", error);
                localStorage.removeItem("token");
                localStorage.removeItem("user");
            } finally {
                setLoading(false);
            }
        };

        checkUserLoggedIn();
    }, []);

    // 2. GİRİŞ YAPMA FONKSİYONU (Login) - GÜNCELLENDİ
    const login = async (formData) => {
        try {
            // Backend'e istek atıyoruz (POST /api/login)
            const response = await api.post("/login", formData);

            // Backend artık { token: "...", user: { id: 1, name: "..." } } dönüyor
            const { token, user } = response.data;

            if (token && user) {
                // Token'ı tarayıcı hafızasına kaydediyoruz
                localStorage.setItem("token", token);

                // Backend'den gelen tam kullanıcı objesini kaydediyoruz (ID, isim, rol dahil)
                localStorage.setItem("user", JSON.stringify(user));

                // State'i güncelliyoruz
                setUser(user);

                toast.success(`Hoş geldiniz, ${user.name}!`);
                return true; // Başarılı olduğunu Login sayfasına bildir
            }
        } catch (error) {
            console.error("Login Error:", error);
            toast.error("Giriş başarısız! Email veya şifre hatalı.");
            return false;
        }
    };

    // 3. ÇIKIŞ YAPMA FONKSİYONU (Logout)
    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        setUser(null);
        toast.info("Çıkış yapıldı.");
    };

    const values = {
        user,
        login,
        logout,
        loading
    };

    return (
        <AuthContext.Provider value={values}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

// Hook olarak dışa aktar (Diğer sayfalarda kolay kullanım için)
export const useAuth = () => useContext(AuthContext);