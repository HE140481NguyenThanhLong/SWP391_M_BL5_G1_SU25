package spring.backend.m_bl5_g1_su25.OnlineShopping.HomeScreen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.response.ProductResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

@Mapper(componentModel = "spring")
public interface HomeMapper {
    @Mapping(target = "status",source = "status")
    @Mapping(target = "name",source = "name")
    @Mapping(target = "description",source="description")
    @Mapping(target="price",source="price")
    ProductResponse toProductResponse(Product product);


}
