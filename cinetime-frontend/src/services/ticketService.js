import api from "./api";

const ticketService = {
    // Aktif Biletler (Gelecek Seanslar)
    getCurrentTickets: async (userId) => {
        const response = await api.get(`/tickets/current/${userId}`);
        return response.data;
    },

    // Geçmiş Biletler
    getPastTickets: async (userId) => {
        const response = await api.get(`/tickets/passed/${userId}`);
        return response.data;
    }
};

export default ticketService;