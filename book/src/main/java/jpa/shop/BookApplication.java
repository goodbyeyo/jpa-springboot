package jpa.shop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }

    @Bean
    Hibernate5Module hibernate5Module() {
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        // json 생성 시점에 강제로 Lazy Loding 실행, 권장하는 방법은 아님, Entity 전체를 노출시키고 싶은 경우 사용...
        // hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
        return new Hibernate5Module();
    }

}
