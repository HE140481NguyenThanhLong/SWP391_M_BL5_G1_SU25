package spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.mapper;

import org.mapstruct.*;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Customer;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.Staff;
import spring.backend.m_bl5_g1_su25.OnlineShopping.UserScreen.entity.User;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Profile.dto.ProfileViewDto;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "userId", source = "user_id")
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "showAdminFields", expression = "java(user.getRole().name().equals(\"ADMIN\"))")
    @Mapping(target = "createdAt", expression = "java(user.getRole().name().equals(\"ADMIN\") ? user.getCreatedAt() : null)")
    @Mapping(target = "updatedAt", expression = "java(user.getRole().name().equals(\"ADMIN\") ? user.getUpdatedAt() : null)")
    ProfileViewDto toProfileViewDto(User user);

    @AfterMapping
    default void setNamesFromCustomer(@MappingTarget ProfileViewDto dto, Customer customer) {
        if (customer != null) {
            dto.setFirstName(customer.getFirstname());
            dto.setLastName(customer.getLastname());
            dto.setFullName(customer.getFirstname() + " " + customer.getLastname());
        }
    }

    @AfterMapping
    default void setNamesFromStaff(@MappingTarget ProfileViewDto dto, Staff staff) {
        if (staff != null) {
            dto.setFirstName(staff.getFirstname());
            dto.setLastName(staff.getLastname());
            dto.setFullName(staff.getFirstname() + " " + staff.getLastname());
        }
    }

    default ProfileViewDto mapUserToProfileView(User user, Customer customer, Staff staff) {
        ProfileViewDto dto = toProfileViewDto(user);
        switch (user.getRole()) {
            case CUSTOMER:
                setNamesFromCustomer(dto, customer);
                break;
            case STAFF:
            case ADMIN:
                setNamesFromStaff(dto, staff);
                break;
        }

        return dto;
    }
}
