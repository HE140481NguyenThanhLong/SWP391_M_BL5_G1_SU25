package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ImportRowError {
    private int rowIndex;     // dòng trong Excel (bắt đầu từ 2 nếu có header)
    private String message;   // mô tả lỗi
}
