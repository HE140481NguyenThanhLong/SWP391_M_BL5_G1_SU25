package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingInformation {
    @Column( nullable = false)
    String recipientName; // Tên người nhận

    @Column( nullable = false)
    String phoneNumber; // Số điện thoại

    // Lưu địa chỉ chi tiết (số nhà, tên đường)
    @Column( nullable = false)
    String streetAddress;

    // Lưu tên Phường/Xã dưới dạng văn bản
    @Column(nullable = false)
    String wardName;

    // Lưu tên Quận/Huyện dưới dạng văn bản
    @Column( nullable = false)
    String districtName;

    // Lưu tên Tỉnh/Thành phố dưới dạng văn bản
    @Column( nullable = false)
    String provinceName;
}
