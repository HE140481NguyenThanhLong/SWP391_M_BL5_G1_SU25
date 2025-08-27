package spring.backend.m_bl5_g1_su25.OnlineShopping;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ URL /temp-uploads/ tới thư mục tạm
        String tmpDir = System.getProperty("java.io.tmpdir");
        registry.addResourceHandler("/temp-uploads/**")
                .addResourceLocations("file:" + tmpDir + "/ecom-uploads/");

        Path uploadDir = Paths.get("src/main/resources/static/uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/" + uploadPath + "/");
    }
}
