package spring.backend.m_bl5_g1_su25.OnlineShopping.PaymentScreen.dto.reponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {
    Integer id;
    String name;
    String description;
    String price;
}
