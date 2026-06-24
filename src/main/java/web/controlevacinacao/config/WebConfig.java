package web.controlevacinacao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import web.controlevacinacao.interceptor.NotificacaoSA2HeaderInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private NotificacaoSA2HeaderInterceptor notificacaoInterceptor;

    public WebConfig(NotificacaoSA2HeaderInterceptor notificacaoInterceptor) {
        this.notificacaoInterceptor = notificacaoInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(notificacaoInterceptor);
    }

}
