import api from "./api";

const showtimeService = {
    // Bir filme ait seansları getir
    getShowtimesByMovie: async (movieId) => {
        const response = await api.get(`/showtimes/movie/${movieId}`);
        return response.data;
    }
};

export default showtimeService;


//Burada film detay sayfasida, o filmin hangi saatlerde oynadigini gostermek gerekiyor.
//Bunun icin Backend kisminda /api/services/showtimes/movie/{id} endpointini kullandik.