package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WardDTO {
    private String ward_code;

    private String name;

    public WardDTO(String ward_code, String name) {
        this.ward_code = ward_code;
        this.name = name;
    }
}