package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportForm;

@Mapper(componentModel = "spring")
public interface CustomerServiceMapper {
        @Mapping(target = "response",source = "response")
        ReportFormResponse toReportForm(ReportFormResponse reportForm);

}
