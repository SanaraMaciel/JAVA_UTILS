
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;

public class AuthenticationUtil {

    private AuthenticationUtil() {
    }

    public static Jwt getPrincipal() {
        // Usuário logado no sistema do contexto do Spring security
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getCurrentUserName() {
        Jwt principal = getPrincipal();
        return (String) principal.getClaims().get(Constants.TOKEN_USER_NAME);
    }

    public static List<String> getCurrentUserRoles() {
        Jwt principal = getPrincipal();
        Map<String, Object> realmAccess = (Map<String, Object>) principal.getClaims().get(Constants.REALM);
        return (List<String>) realmAccess.get(Constants.ROLES);
    }

    public static String getToken() {
        return getPrincipal().getTokenValue();
    }
}
