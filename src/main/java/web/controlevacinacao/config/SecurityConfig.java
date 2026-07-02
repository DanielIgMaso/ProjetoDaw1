package web.controlevacinacao.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> configurer

                        .requestMatchers("/favicon.ico", "/css/**", "/js/**", "/images/**", "/",
                                "/index.html", "/login", "/login-error",
                                "/layout/header-atualizado", "/layout/menu-atualizado")
                        .permitAll()


                        .requestMatchers("/produto/cadastrar").hasRole("ADMIN")
                        .requestMatchers("/usuario/cadastrar").hasRole("ADMIN")
                        .requestMatchers("/relatorios/todospedidos").hasRole("ADMIN")
                        .anyRequest().authenticated()).formLogin(form -> form

                        .loginPage("/login")

                        .defaultSuccessUrl("/index.html").successHandler(successHandler())

                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if ("true".equals(request.getHeader("HX-Request"))) {
                                response.addHeader("HX-Trigger", "atualizarAuth");
                                String hxLocationPayload = "{\"path\":\"/\", \"target\":\"#main\", \"swap\":\"outerHTML\"}";
                                response.addHeader("HX-Location", hxLocationPayload);
                                response.setStatus(HttpServletResponse.SC_OK);
                            } else {
                                response.sendRedirect("/");
                            }
                        }).invalidateHttpSession(true).deleteCookies("JSESSIONID"))
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .csrf((csrf) -> csrf
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()));
        return http.build();
    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String targetUrl = "/index.html";
            boolean isDeepLink = false;
            RequestCache requestCache = new HttpSessionRequestCache();
            SavedRequest savedRequest = requestCache.getRequest(request, response);
            if (savedRequest != null) {
                targetUrl = savedRequest.getRedirectUrl();
                requestCache.removeRequest(request, response);
                isDeepLink = true;
            }
            if (isDeepLink) {
                String hxLocationPayload = String
                        .format("{\"path\":\"%s\", \"target\":\"#main\", \"swap\":\"outerHTML\"}", targetUrl);
                response.addHeader("HX-Location", hxLocationPayload);
                response.addHeader("HX-Trigger", "atualizarAuth");
            } else {
                response.addHeader("HX-Redirect", targetUrl);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        };
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
                "select nome_usuario, senha, ativo " + "from usuario " + "where nome_usuario = ?");
        manager.setAuthoritiesByUsernameQuery("SELECT tab.nome_usuario , papel.nome FROM"
                + "(SELECT usuario.nome_usuario , usuario.codigo FROM usuario WHERE nome_usuario = ?) as tab "
                + " INNER JOIN usuario_papel ON codigo_usuario = tab.codigo "
                + " INNER JOIN papel ON codigo_papel = papel.codigo;");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {





        String idEnconder = "argon2";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idEnconder, encoders);
        return passwordEncoder;
    }

}

final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
    private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       Supplier<CsrfToken> csrfToken) {
        this.xor.handle(request, response, csrfToken);
        csrfToken.get();
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        return this.xor.resolveCsrfTokenValue(request, csrfToken);
    }
}