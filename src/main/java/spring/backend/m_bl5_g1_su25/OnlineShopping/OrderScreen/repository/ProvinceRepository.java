package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.ProvinceDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Province;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Ward;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
    @Query("SELECT new spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.ProvinceDTO(p.province_code, p.name) FROM Province p")
    List<ProvinceDTO> findAlls();
}
