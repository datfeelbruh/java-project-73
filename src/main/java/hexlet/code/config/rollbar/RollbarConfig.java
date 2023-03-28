package hexlet.code.config.rollbar;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
    "hexlet.code.app"
})
public class RollbarConfig {
    @Value("${rollbar_token:}")
    private String rollbarToken;
    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Bean
    public Rollbar rollbar() {
        return new Rollbar(getRollbarConfig(rollbarToken));
    }

    private Config getRollbarConfig(String rollbarToken) {
        return RollbarSpringConfigBuilder.withAccessToken(rollbarToken)
                .environment("development")
                .enabled(activeProfile.equals("dev"))
                .build();
    }
}
