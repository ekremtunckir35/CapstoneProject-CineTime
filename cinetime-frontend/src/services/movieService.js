import api from "./api";

const movieService = {
    // 1. Vizyondaki Filmleri Getir
    getInTheaters: async () => {
        const response = await api.get("/movies/in-theaters");
        return response.data;
    },

    // 2. Yakında Gelecek Filmleri Getir (HATA BURADAYDI)
    getComingSoon: async () => {
        const response = await api.get("/movies/coming-soon"); // Buraya 'const response =' yazdığımızdan emin olalım
        return response.data;
    },

    // 3. Tüm Filmleri Getir (Arama ve Sayfalama ile)
    getAllMovies: async (page = 0, query = "") => {
        const response = await api.get(`/movies?page=${page}&q=${query}`);
        return response.data;
    },

    // 4. Tek bir filmin detayını getir
    getMovieById: async (id) => {
        const response = await api.get(`/movies/${id}`);
        return response.data;
    },
    deleteMovie: async (id) => {
        return await api.delete(`/movies/${id}`);
    },

    createMovie: async (movieData) => {
        return await api.post("/movies", movieData);
    }
};

export default movieService;