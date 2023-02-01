package com.spring.board.config;

// import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
//import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
// @EnableConfigurationProperties(ThymeleafConfig.class)
public class ThymeleafConfig {

    @Bean
    // @ConditionalOnMissingBean   // Bean  없을 경우에만 Bean 등록 thymeleaf3Properties
    public SpringResourceTemplateResolver thymeleafTemplateResolver(
            SpringResourceTemplateResolver defaultTemplateResolver,
            Thymeleaf3Properties thymeleaf3Properties
    ) {
        defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());

        return defaultTemplateResolver;
    }


    @RequiredArgsConstructor
    @Getter
    @ConstructorBinding   // 생성자 바인딩
    @ConfigurationProperties("spring.thymeleaf3") // application.yml 에서 spring.thymeleaf3 를 찾아서 바인딩
    public static class Thymeleaf3Properties {
        /**
         * Use Thymeleaf 3 Decoupled Logic
         */
        private final boolean decoupledLogic;

    }
}
