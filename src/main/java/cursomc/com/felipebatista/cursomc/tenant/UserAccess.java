package cursomc.com.felipebatista.cursomc.tenant;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Objects;

/**
 * O acesso a uma aplicação é definido por quatro elementos: sistema, entidade,
 * database e usuário. O acesso a um usuário é concedido de duas formas, quando
 * ocorre uma liberação de uma entidade, onde o usuário que realiza a liberação
 * recebe um acesso para administrar a entidade liberada e, quando o
 * administrador da entidade concede um acesso a um usuário. Esta classe
 * representa o acesso de um usuário, construído a cada requisição através de um
 * conjunto de informações emitidas pelo consumidor de um recurso.
 *
 */
public class UserAccess implements Serializable {

    private Long sourceSystemId;
    private Long systemId;
    private Long entityId;
    private Long databaseId;
    private String userId;
    private String sourceUserId;

    private UserAccess(final Long systemId, final Long entityId, final Long databaseId, final String userId) {
        this(systemId, systemId, entityId, databaseId, userId, userId);
    }

    private UserAccess(final Long systemId, final Long sourceSystemId, final Long entityId, final Long databaseId, final String userId, final String sourceUserId) {
        Objects.requireNonNull(systemId);
        Objects.requireNonNull(sourceSystemId);
        Objects.requireNonNull(entityId);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(sourceUserId);
        Objects.requireNonNull(databaseId);
        this.systemId = systemId;
        this.sourceSystemId = sourceSystemId;
        this.entityId = entityId;
        this.databaseId = databaseId;
        this.userId = userId;
        this.sourceUserId = sourceUserId;
    }

    protected UserAccess() {
    }

    public static UserAccess empty() {
        return new UserAccess();
    }

    public static UserAccess anonymous() {
        UserAccess userAccess = new UserAccess();
        userAccess.userId = "anonymous";

        return userAccess;
    }

    public static UserAccess of(final String hash, final String userId, final Long systemId) throws EncryptionException {
        return UserAccessEntityHash.decode(hash, userId, systemId).getUserAccess();
    }

    public static UserAccess of(final Long systemId, final Long entityId, final Long databaseId, final String userId) {
        return new UserAccess(systemId, entityId, databaseId, userId);
    }

    static UserAccess of(final Long systemId, final Long sourceSystemId, final Long entityId, final Long databaseId, final String userId, final String sourceUserId) {
        return new UserAccess(systemId, sourceSystemId, entityId, databaseId, userId, sourceUserId);
    }

    boolean hasSourceSystemaId() {
        return !systemId.equals(sourceSystemId);

    }

    boolean hasSourceUserId() {
        return !userId.equals(sourceUserId);
    }

    Long getSourceSystemId() {
        return sourceSystemId;
    }

    String getSourceUserId() {
        return sourceUserId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public Long getDatabaseId() {
        return databaseId;
    }

    public String getUserId() {
        return userId;
    }

    public String toHash() {
        if (systemId == null || entityId == null) {
            throw new IllegalStateException("Não é possível gerar um hash para um usuário anônimo ou vazio.");
        }
        try {
            return UserAccessEntityHash.encode(this).getHash();
        } catch (EncryptionException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        return "UserAccess{" + "systemId=" + systemId + ", entityId=" + entityId + ", databaseId=" + databaseId + ", userId=" + userId + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserAccess that = (UserAccess) o;

        return new EqualsBuilder()
                .append(sourceSystemId, that.sourceSystemId)
                .append(systemId, that.systemId)
                .append(entityId, that.entityId)
                .append(databaseId, that.databaseId)
                .append(userId, that.userId)
                .append(sourceUserId, that.sourceUserId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sourceSystemId)
                .append(systemId)
                .append(entityId)
                .append(databaseId)
                .append(userId)
                .append(sourceUserId)
                .toHashCode();
    }
}
