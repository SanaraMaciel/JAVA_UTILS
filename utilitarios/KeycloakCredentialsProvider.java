

/**
 * Classe que obtem atributos do token jwt do keycloak
 * 
 * @author rsoprano
 *
 */

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import br.com.cpqd.pcri.apiadm.constants.Keycloak;

@Component
public class KeycloakCredentialsProvider implements Authentication {
	private static final long serialVersionUID = 1L;

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}	
	
	public String getIdKeycloak() {
	    Jwt principal = getPrincipal();
        return (String) principal.getClaims().get(Keycloak.IDKEYCLOAK);
	}
	
	public String getEmail() {
	    Jwt principal = getPrincipal();
        return (String) principal.getClaims().get(Keycloak.EMAIL);
	}
	
	public String getCompletedName() {
	    Jwt principal = getPrincipal();
        return (String) principal.getClaims().get(Keycloak.NAME);
	}
	
	public String getCompany() {
	    Jwt principal = getPrincipal();
        return (String) principal.getClaims().get(Keycloak.COMPANY);
	}	
	
	public boolean isAdmin() {
		return false;
	}	
	
	@Override
	public String getName() {
	    Jwt principal = getPrincipal();
        return (String) principal.getClaims().get(Keycloak.PREFERRED_USERNAME);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Authentication authentication = getAuthentication();
		return authentication.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		Authentication authentication = getAuthentication();
		return authentication.getCredentials();
	}

	@Override
	public Object getDetails() {
		Authentication authentication = getAuthentication();
		return authentication.getDetails();
	}

	@Override
	public Jwt getPrincipal() {
		Authentication authentication = getAuthentication();
		return (Jwt) authentication.getPrincipal();
	}

	@Override
	public boolean isAuthenticated() {
		return false;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}
	

}
