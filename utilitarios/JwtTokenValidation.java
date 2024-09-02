
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JwtTokenValidation {

    private JwtToken jwtToken;

    public boolean validationRole(JwtToken jwtToken, String role) {
        boolean hasRole = false;

        this.setJwtToken(jwtToken);

        RealmAccess realmAccess = this.getJwtToken().getRealmAccess();

        if (Objects.nonNull(realmAccess) && realmAccess.getRoles().contains(role)) {
            hasRole = realmAccess.getRoles().get(realmAccess.getRoles().indexOf(role)).equals(role);
        }

        return hasRole;
    }

    public boolean validationRole(Jwt jwt, String role) {
        this.setJwtToken(jwt);
        
        return this.validationRole(this.getJwtToken(), role);
    }

    public boolean validationRegional(JwtToken jwtToken) {
        boolean hasRegional = false;

        this.setJwtToken(jwtToken);

        for (String regional : RegionalType.getTypes()) {
            if (Objects.nonNull(this.getJwtToken().getRegional()) && this.getJwtToken().getRegional().contains(regional)) {
                hasRegional = this.getJwtToken().getRegional().get(this.getJwtToken().getRegional().indexOf(regional)).equals(regional);
            }
        }

        return hasRegional;
    }

    public boolean validationRegional(Jwt jwt) {
        this.setJwtToken(jwt);

        return this.validationRegional(this.getJwtToken());
    }

    private void setJwtToken(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }
    
    private void setJwtToken(Jwt jwt) {
        this.jwtToken = new JwtToken(jwt);
    }

    private JwtToken getJwtToken() {
        return this.jwtToken;
    }
}
