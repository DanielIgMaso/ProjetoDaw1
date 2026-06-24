package web.controlevacinacao.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.spring6.view.ThymeleafView;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ControllerHtmxReturnAspect {

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllersPointcut() {}

    @Around("controllersPointcut()")
    public Object changeReturnIfHtmxIsUsed(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String headerHtmx = request.getHeader("HX-Request");
        Object result = joinPoint.proceed();
        String retorno = getReturnString(result);
        if (headerHtmx == null) {
            if (retorno != null) {  // no caso do relatorio
                int posicao = retorno.indexOf("::");
                if (posicao != -1) {
                    return retorno.substring(0, posicao).trim();
                }
            }
        }
        return result;
    }

    private String getReturnString(Object result) {
        String nomeDaView = null;
        if (result instanceof ModelAndView) {
            ModelAndView retorno = (ModelAndView) result;
            nomeDaView = retorno.getViewName();
        } else if (result instanceof ThymeleafView) {
            ThymeleafView retorno = (ThymeleafView) result;
            nomeDaView = retorno.getTemplateName();
        } else if (result instanceof String) {
            nomeDaView = (String) result;
        }

        return nomeDaView;
    }
}
