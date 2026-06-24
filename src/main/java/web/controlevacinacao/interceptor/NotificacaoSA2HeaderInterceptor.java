package web.controlevacinacao.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;
import web.controlevacinacao.notification.NotificacaoSweetAlert2;

@Component
public class NotificacaoSA2HeaderInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    public NotificacaoSA2HeaderInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        if (modelAndView != null && modelAndView.getModel() != null) {
            Object notificacaoObj = modelAndView.getModel().get("notificacaoSA2");

            if (notificacaoObj instanceof NotificacaoSweetAlert2) {
                NotificacaoSweetAlert2 notificacaoSA2 = (NotificacaoSweetAlert2) notificacaoObj;
                
                // Estrutura do evento: {"exibirAlerta": { ... dados ... }}
                String jsonDados = objectMapper.writeValueAsString(notificacaoSA2);
                String headerValue = "{\"exibirAlerta\": " + jsonDados + "}";

                response.addHeader("HX-Trigger", headerValue);
            }
        }
    }
}
