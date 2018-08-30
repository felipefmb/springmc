package cursomc.com.felipebatista.cursomc.tenant;

import java.text.MessageFormat;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    static final String DEFAULT_TENANT = "-1#-1";

    static final String TENANT_MASK = "{0}#{1}";

    @Override
    public String resolveCurrentTenantIdentifier() {
        if (!ContextProvider.hasContext()) {
            return DEFAULT_TENANT;
        }
        AuthenticationHolder authenticationHolder = ContextProvider.getBean(AuthenticationHolder.class);
        UserAccess userAccess = authenticationHolder.getUserAccess();

        return (userAccess != null) ? getTenant(userAccess) : DEFAULT_TENANT;
    }

    private String getTenant(UserAccess userAccess) {
        if (userAccess.getDatabaseId() == null) {
            return MessageFormat.format(TENANT_MASK, "-1", "-1");
        }
        return MessageFormat.format(TENANT_MASK, userAccess.getDatabaseId().toString(), userAccess.getEntityId().toString());
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
