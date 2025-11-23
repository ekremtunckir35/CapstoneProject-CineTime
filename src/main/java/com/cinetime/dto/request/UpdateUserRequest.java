package com.cinetime.dto.request;

//Kullanici kendi profilini guncelleyebilmesi icin
import lombok.Data;

@Data
public class UpdateUserRequest {


        private String name;
        private String surname;
        private String email;
        private String phoneNumber;

}
