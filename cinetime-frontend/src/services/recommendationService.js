import api from "./api";

const recommendationService = {
    getRecommendations: async (userId) => {
        const response = await api.get(`/recommendations/${userId}`);
        return response.data;
    }
};

export default recommendationService;