package spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.dto.response.ReportFormResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.entity.ReportForm;
import spring.backend.m_bl5_g1_su25.OnlineShopping.CustomerService.enums.ReportStatus;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerServiceMapper {
        @Mapping(target = "title",source = "title")
        @Mapping(target = "description",source = "description")
        @Mapping(target = "imageUrl",source = "imgUrl")
        @Mapping(target = "issueType",source = "issueType")
        @Mapping(target = "status",source = "status")
        @Mapping(target = "createdAt",source = "createdAt")
        @Mapping(target = "resolvedAt",source = "resolvedAt")
        ReportFormResponse toReportFormResponse(ReportForm reportForm);

}
