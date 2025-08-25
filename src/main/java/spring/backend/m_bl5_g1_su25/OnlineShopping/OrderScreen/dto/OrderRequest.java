package spring.backend.m_bl5_g1_su25.OnlineShopping.OrderScreen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.enums.PaymentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String name;          // tên người nhận
    private String email;
    private String phone;
    private String country;
    private String province;      // province_code
    private String ward;          // ward_code
    private String address;       // số nhà, tên đường

    private PaymentType paymentType = PaymentType.CASH_ON_DELIVERY;
}

