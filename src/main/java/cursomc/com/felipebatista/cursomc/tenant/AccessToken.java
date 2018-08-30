
package cursomc.com.felipebatista.cursomc.tenant;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Esta classe representa um token de acesso gerado pelo servidor de autorização OAuth pelo endpoint /oauth2/authorize.
 */
public final class AccessToken implements Serializable {

    private static final Pattern PATTERN_FOUND_AUTH_HEADER = Pattern
            .compile("(\\s*Authorization\\s*:\\s*bearer\\s*|\\s*bearer\\s*).*", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_REPLEACE_AUTH_HEADER_TOKEN = Pattern
            .compile("(\\s*Authorization\\s*:\\s*bearer\\s*|\\s*bearer\\s)", Pattern.CASE_INSENSITIVE);

    private final String token;
    private Properties properties;

    public AccessToken(String token) {
        this.token = token;
    }

    /**
     * Obtem um objeto contendo detalhes do access token.
     *
     * @return os detalhes de um access token.
     */
    public final Properties getProperties() {
        return properties;
    }

    /**
     * Obtem um token a partir do header HTTP Authorization considerando a estratégia definida pela especificação OAuth.
     *
     * @param authorizationHeader
     * @return access token
     */
    public static String getTokenByAuthorizationHeader(String authorizationHeader) {
        String tokenValue = PATTERN_REPLEACE_AUTH_HEADER_TOKEN.matcher(authorizationHeader).replaceAll("");
        if (tokenValue == null || tokenValue.isEmpty() || !PATTERN_FOUND_AUTH_HEADER.matcher(authorizationHeader).matches()) {
            throw new IllegalArgumentException(String.format("O header \"Authorization: Bearer\" é inválido - (%s).", tokenValue));
        }

        return tokenValue;
    }

    /**
     * Retorna a representação alfanumérica do access token.
     *
     * @return access token
     */
    @Override
    public final String toString() {
        return token;
    }

    /**
     * Aplica um validador responsável em checar a integridade de um access token.
     *
     * @param validator contendo o mecanismo que efetua a checagem da integridade do token.
     */
    public void validate(TokenValidator validator, ScopeValidationContext context) {
        properties = validator.validate(this.toString(), context);
    }

    /**
     * Representação para os detalhes de um access token, oriundos de um autorization server.
     */
    public interface Properties extends Serializable {

        /**
         * Obtem o id do sistema ao qual o token foi emitido.
         *
         * @return id do sistema
         */
        Integer getSystemId();

        /**
         * Verifica se o access token não existe.
         *
         * @return true caso o access token não exista.
         */
        boolean isNotFound();

        /**
         * Verifica se o access token está expirado.
         *
         * @return true caso o access token esta expirado.
         */
        boolean isExpired();

        /**
         * Obtem o id do usuário ao qual o access token foi emitido.
         *
         * @return id do usuário.
         */
        String getUser();

        /**
         * Obtem os atributos de um usuário ao qual o access token foi emitido.
         *
         * @return atributos do usuário.
         */
        Map<String, String> getUserAttributes();

        /**
         * Obtem a identificação do cliente que emitiu o access token.
         *
         * @return clientId.
         */
        String getClientId();

        /**
         * Obtem o tempo de expiração do access token em milisegundos.
         *
         * @return o tempo de expiração em milisegundos
         */
        Long getExpiresIn();

        /**
         * Obtem os escopos de acesso para o access token emitido.
         *
         * @return os escopos de acesso.
         */
        List<String> getScopes();

        /**
         * Obtem a descrição caso ocorra um algum erro durante a checagem de integridade de um access token.
         *
         * @return descrição de um erro.
         */
        String getError();
    }
}
