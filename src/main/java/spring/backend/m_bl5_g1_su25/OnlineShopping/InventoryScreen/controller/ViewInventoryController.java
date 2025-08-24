package spring.backend.m_bl5_g1_su25.OnlineShopping.InventoryScreen.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.backend.m_bl5_g1_su25.OnlineShopping.InventoryScreen.service.ViewInventoryService;

@Slf4j
@Controller
@RequestMapping("/staff")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ViewInventoryController {

    ViewInventoryService viewInventoryService;

    @GetMapping("/inventory-detail")
    public String getInventoryDetail(
            @RequestParam(value = "status", defaultValue = "all") String status,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        var inventoryData = viewInventoryService.getInventoryStatistics(status, keyword);
        model.addAttribute("totalProducts", inventoryData.getTotalProducts());
        model.addAttribute("lowStockProducts", inventoryData.getLowStockProducts());
        model.addAttribute("outOfStockProducts", inventoryData.getOutOfStockProducts());
        model.addAttribute("inventoryProducts", inventoryData.getInventoryProducts());
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentKeyword", keyword != null ? keyword : "");
        return "staff/inventory-detail";
    }
}
