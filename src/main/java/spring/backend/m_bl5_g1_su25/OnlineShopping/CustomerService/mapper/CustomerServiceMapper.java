package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.mapper;

import org.mapstruct.Mapper;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormDefault;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormResponseForStaff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormResponseForCustomer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportForm;

@Mapper(componentModel = "spring")
public interface CustomerServiceMapper {

        ReportFormResponseForStaff toReportForm(ReportForm reportForm);


        ReportFormResponseForCustomer toReportFormForCustomer(ReportForm reportForm);
        ReportFormDefault toReportFormDefault(ReportForm reportForm);

}
