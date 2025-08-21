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
    default void setNamesFromCustomer(@MappingTarget ProfileViewDto dto, User user, Customer customer) {
        if (customer != null) {
            dto.setFirstName(customer.getFirstname());
            dto.setLastName(customer.getLastname());
            dto.setFullName(customer.getFirstname() + " " + customer.getLastname());
        }
    }

    @AfterMapping
    default void setNamesFromStaff(@MappingTarget ProfileViewDto dto, User user, Staff staff) {
        if (staff != null) {
            dto.setFirstName(staff.getFirstname());
            dto.setLastName(staff.getLastname());
            dto.setFullName(staff.getFirstname() + " " + staff.getLastname());
        }
    }

    // Helper methods for role checking
    default boolean isAdmin(User user) {
        return user.getRole().name().equals("ADMIN");
    }

    // Composite mapping method that handles the complete mapping including related entities
    default ProfileViewDto mapUserToProfileView(User user, Customer customer, Staff staff) {
        ProfileViewDto dto = toProfileViewDto(user);

        // STAFF and CUSTOMER have the same security level - both show basic info only
        switch (user.getRole()) {
            case CUSTOMER:
                setNamesFromCustomer(dto, user, customer);
                break;
            case STAFF:
            case ADMIN:
                setNamesFromStaff(dto, user, staff);
                break;
        }

        return dto;
    }
}
