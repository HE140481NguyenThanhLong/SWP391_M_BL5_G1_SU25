package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.service;

import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.ProvinceDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.WardDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Province;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Ward;

import java.util.List;

public interface AddressService {
    public List<ProvinceDTO> getAllProvinces();
    public List<WardDTO> getWardsByProvince(String provinceCode) ;
}
