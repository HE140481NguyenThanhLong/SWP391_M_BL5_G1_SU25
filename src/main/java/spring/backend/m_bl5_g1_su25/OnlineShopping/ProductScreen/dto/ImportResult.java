package spring.backend.m_bl5_g1_su25.OnlineShopping.ProductScreen.dto;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ImportResult {
    private int total;             // tổng số dòng đọc
    private int success;           // số bản ghi lưu thành công
    @Builder.Default
    private List<ImportRowError> errors = new ArrayList<>();

    public void addError(int row, String msg) {
        errors.add(ImportRowError.builder().rowIndex(row).message(msg).build());
    }
}
