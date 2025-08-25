package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Ward;

import java.util.Set;

@Getter
@Setter
public class ProvinceDTO {
    private String province_code;

    private String name;

    public ProvinceDTO(String province_code, String name) {
        this.province_code = province_code;
        this.name = name;
    }
}
