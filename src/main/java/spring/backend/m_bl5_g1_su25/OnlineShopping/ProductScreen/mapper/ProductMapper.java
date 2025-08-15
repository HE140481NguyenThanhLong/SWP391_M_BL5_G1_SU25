package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto.reponse.ProductResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "name",source = "name")
    @Mapping(target = "description",source="description")
    @Mapping(target="price",source="price")
    ProductResponse toProductResponse(Product product);


}
