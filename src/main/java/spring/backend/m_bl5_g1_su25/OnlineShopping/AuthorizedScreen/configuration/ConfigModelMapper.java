package spring.backend.m_bl5_g1_su25.OnlineShopping.AuthorizedScreen.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigModelMapper {
    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }
}
