import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const Navbar = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-black py-3 border-bottom border-secondary">
            <div className="container">
                {/* Logo */}
                <Link className="navbar-brand fw-bold text-primary fs-3" to="/">
                    🍿 CineTime
                </Link>

                {/* Mobil Menü Butonu */}
                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarNav"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <Link className="nav-link active" to="/">Ana Sayfa</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/movies">Filmler</Link>
                        </li>
                    </ul>

                    {/* Sağ Taraf: Giriş Durumuna Göre Değişen Alan */}
                    <div className="d-flex align-items-center">
                        {user ? (
                            // KULLANICI GİRİŞ YAPMIŞSA
                            <div className="dropdown">
                                <button
                                    className="btn btn-outline-light dropdown-toggle d-flex align-items-center gap-2"
                                    type="button"
                                    data-bs-toggle="dropdown"
                                >
                                    <div className="bg-primary rounded-circle d-flex justify-content-center align-items-center"
                                         style={{ width: "30px", height: "30px" }}>
                                        {user.email?.charAt(0).toUpperCase()}
                                    </div>
                                    <span>{user.email?.split("@")[0]}</span>
                                </button>
                                <ul className="dropdown-menu dropdown-menu-dark dropdown-menu-end">
                                    <li><Link className="dropdown-item" to="/profile">Biletlerim</Link></li>
                                    <li><hr className="dropdown-divider" /></li>
                                    <li>
                                        <button className="dropdown-item text-danger" onClick={handleLogout}>
                                            Çıkış Yap
                                        </button>
                                    </li>
                                </ul>
                            </div>
                        ) : (
                            // KULLANICI GİRİŞ YAPMAMIŞSA
                            <div className="d-flex gap-2">
                                <Link to="/login" className="btn btn-outline-light">Giriş</Link>
                                <Link to="/register" className="btn btn-primary">Kayıt Ol</Link>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;