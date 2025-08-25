package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.ProvinceDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.WardDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.service.AddressService;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Province;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Ward;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/provinces")
    public List<ProvinceDTO> getProvinces() {
        return addressService.getAllProvinces();
    }

    @GetMapping("/wards/{provinceCode}")
    public List<WardDTO> getWards(@PathVariable String provinceCode) {
        return addressService.getWardsByProvince(provinceCode);
    }
}