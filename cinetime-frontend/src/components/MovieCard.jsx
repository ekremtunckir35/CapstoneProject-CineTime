//Filmleri listelerken her bir film için aynı kodu tekrar yazmamak adına
// bir bileşen oluşturduk

import { Link } from "react-router-dom";

const MovieCard = ({ movie }) => {
    return (
        <div className="col">
            <div className="card h-100 bg-dark text-white border-secondary shadow-sm movie-card">
                {/* Film Posteri */}
                <div className="position-relative overflow-hidden">
                    {/* Resim yoksa varsayılan bir görsel gösterelim */}
                    <img
                        src={movie.poster.startsWith("http")
                            ? movie.poster
                            : `http://localhost:8080/api/images/${movie.poster}`}
                        className="card-img-top"
                        alt={movie.title}
                        style={{ objectFit: "cover", height: "400px" }}
                    />
                    <div className="card-img-overlay d-flex flex-column justify-content-end p-0">
                        <div className="bg-gradient-dark p-3 w-100" style={{background: "linear-gradient(to top, black, transparent)"}}>
                            <span className="badge bg-primary mb-2">{movie.genre}</span>
                        </div>
                    </div>
                </div>

                <div className="card-body">
                    <h5 className="card-title text-truncate" title={movie.title}>{movie.title}</h5>
                    <div className="d-flex justify-content-between align-items-center text-secondary small">
                        <span>⏱ {movie.duration} dk</span>
                        <span>⭐ {movie.rating || "Yeni"}</span>
                    </div>
                </div>

                <div className="card-footer bg-transparent border-top-0">
                    <Link to={`/movie/${movie.id}`} className="btn btn-outline-primary w-100">
                        Bilet Al
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default MovieCard;