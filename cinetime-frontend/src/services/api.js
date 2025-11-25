import axios from "axios";

//1-TEMEL AYARLAR

const  api = axios.create({
    baseURL: "http://localhost:8080/api/", //BACKEND ADRESIMIZ

    headers :{
        "Content-Type": "application/json",
    },
});


//2-ISTEK ONCESI (INTERCEPTORS) -TOKEN EKLEME MANTIGI
//Her istek atildiginda Äcaba token var mi? diye bakar

api.interceptors.request.use((config) => {

    const token = localStorage.getItem("token");//Tokení tarayici hafizasindan al
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;//Varsa header e ekle

    }
    return config;

},
    (error) =>{
        return Promise.reject(error);

    }

    );
export default api;

//BU DOSYA BACKEND ILE BAGLANTILI DOSYAMIZ.GIRIS YAPMIS BIR KULLANICI ISTEK ATTIGINDA ,