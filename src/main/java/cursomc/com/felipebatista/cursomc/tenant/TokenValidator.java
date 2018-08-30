package cursomc.com.felipebatista.cursomc.tenant;

public interface TokenValidator {
    AccessToken.Properties validate(String token, ScopeValidationContext context);
}