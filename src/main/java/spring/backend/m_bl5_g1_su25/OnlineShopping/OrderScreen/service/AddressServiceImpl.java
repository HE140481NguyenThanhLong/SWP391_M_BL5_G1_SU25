package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.ProvinceDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.WardDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository.ProvinceRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository.WardRepository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Province;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Ward;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final ProvinceRepository provinceRepository;
    private final WardRepository wardRepository;

    public List<ProvinceDTO> getAllProvinces() {
        return provinceRepository.findAlls();
    }

    public List<WardDTO> getWardsByProvince(String provinceCode) {
        return wardRepository.findByProvinces_Province_code(provinceCode);
    }
}
