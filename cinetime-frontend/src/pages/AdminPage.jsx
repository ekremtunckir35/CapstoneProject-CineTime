import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import movieService from "../services/movieService";
import { toast } from "react-toastify";

const AdminPage = () => {
    const [movies, setMovies] = useState([]);

    // Yeni Film Formu
    const [newMovie, setNewMovie] = useState({
        title: "", summary: "", duration: 120, genre: "Dram",
        releaseDate: "01-01-2025", poster: "", director: "Bilinmiyor"
    });

    const fetchMovies = async () => {
        const data = await movieService.getAllMovies(0, ""); // İlk sayfayı çek
        setMovies(data.content); // Page yapısında veri 'content' içindedir
    };

    useEffect(() => { fetchMovies(); }, []);

    const handleDelete = async (id) => {
        if(confirm("Bu filmi silmek istediğine emin misin?")) {
            try {
                await movieService.deleteMovie(id);
                toast.success("Film silindi.");
                fetchMovies(); // Listeyi yenile
            } catch (e) { toast.error("Hata oluştu."); }
        }
    };

    const handleCreate = async (e) => {
        e.preventDefault();
        try {
            // Basit bir DTO çevrimi (Formats ve Cast şimdilik boş)
            await movieService.createMovie({
                ...newMovie,
                cast: ["Oyuncu 1"],
                formats: ["2D"],
                status: "IN_THEATERS" // Varsayılan vizyonda
            });
            toast.success("Film eklendi!");
            fetchMovies();
        } catch (e) { toast.error("Ekleme hatası."); }
    };

    return (
        <div className="bg-dark min-vh-100 text-white">
            <Navbar />
            <div className="container py-5">
                <h2 className="text-danger">👑 Admin Paneli</h2>

                {/* Film Ekleme Formu */}
                <div className="card bg-secondary p-3 mb-5">
                    <h4>Yeni Film Ekle</h4>
                    <form onSubmit={handleCreate} className="row g-3">
                        <div className="col-md-6"><input className="form-control" placeholder="Film Adı" onChange={e=>setNewMovie({...newMovie, title:e.target.value})} required/></div>
                        <div className="col-md-6"><input className="form-control" placeholder="Tür" onChange={e=>setNewMovie({...newMovie, genre:e.target.value})} required/></div>
                        <div className="col-12"><textarea className="form-control" placeholder="Özet" onChange={e=>setNewMovie({...newMovie, summary:e.target.value})} required/></div>
                        <div className="col-12"><button className="btn btn-success w-100">Kaydet</button></div>
                    </form>
                </div>

                {/* Film Listesi */}
                <h4>Mevcut Filmler</h4>
                <table className="table table-dark table-hover">
                    <thead><tr><th>ID</th><th>Ad</th><th>Yönetmen</th><th>İşlem</th></tr></thead>
                    <tbody>
                    {movies.map(m => (
                        <tr key={m.id}>
                            <td>{m.id}</td>
                            <td>{m.title}</td>
                            <td>{m.director}</td>
                            <td><button onClick={() => handleDelete(m.id)} className="btn btn-sm btn-danger">Sil</button></td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default AdminPage;