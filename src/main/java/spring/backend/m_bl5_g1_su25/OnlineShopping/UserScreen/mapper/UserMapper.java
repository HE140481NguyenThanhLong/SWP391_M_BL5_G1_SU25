package spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.dto.response.UserResponse;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "created_at", source = "createdAt")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    UserResponse toUserResponse(User user);

}
