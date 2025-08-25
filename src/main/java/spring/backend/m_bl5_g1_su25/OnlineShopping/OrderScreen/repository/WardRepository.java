package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.WardDTO;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Ward;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    @Query("""
    SELECT new spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto.WardDTO(w.ward_code, w.name) FROM Ward w
    where w.provinces.province_code=:provinceCode
        """)
    List<WardDTO> findByProvinces_Province_code(@Param("provinceCode") String provinceCode);
}