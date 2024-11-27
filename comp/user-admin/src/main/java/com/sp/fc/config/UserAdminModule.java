package com.sp.fc.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.sp.fc.user")
@EntityScan(basePackages = {
        "com.sp.fc.user.domain"
})
@EnableJpaRepositories(basePackages = {
        "com.sp.fc.user.repository"
})
public class UserAdminModule {

    /**
     * 이렇게 설정해 두면 앞으로 config 디렉토리만 스캔해도 안에 configuration 이 있기 때문에
     * Entity 스캔과 Repository 스캔을 동시에 수행할 수 있음.
     *
     * @SpringBootApplication(scanBasePackages = {
     *         "com.sp.fc.config",
     *         "com.sp.fc.web"
     * })
     */

}
