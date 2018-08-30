package cursomc.com.felipebatista.cursomc.tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;

public class ConnectionProvider extends DatasourceConnectionProviderImpl implements MultiTenantConnectionProvider {

    private static final String SELECT_CONTEXT = "select set_config('context.i_databases' ,? , false), set_config('context.i_entidades' ,? , false)";
    private static final Log log = LogFactory.getLog(ConnectionProvider.class);

    public ConnectionProvider() {
    }

    public Connection getAnyConnection() throws SQLException {
        return this.getConnection(null);
    }

    public Connection getConnection() throws SQLException {
        throw new UnsupportedOperationException("Não é possível adquirir uma conexão sem definir um tenant.");
    }

    public Connection getConnection(String tenantIdentifier) throws SQLException {
        String databaseIdentifier = "-1";
        String entityIdentifier = "-1";
        if (StringUtils.isNotBlank(tenantIdentifier)) {
            String[] tenant = tenantIdentifier.split("#");
            databaseIdentifier = tenant[0];
            entityIdentifier = tenant[1];
        }
        if (log.isTraceEnabled()) {
            log.trace("getConnection -> tenant(" + databaseIdentifier + ")");
        }

        Connection connection = super.getConnection();
        if (log.isTraceEnabled()) {
            log.trace("getConnection -> setDatabase(" + databaseIdentifier + ")");
        }

        setContext(databaseIdentifier, entityIdentifier, connection);
        return connection;
    }

    private void setContext(String databaseIdentifier, String entityIdentifier,
            Connection connection) throws SQLException {
        try {
            PreparedStatement cstmt = null;

            try {
                cstmt = connection.prepareStatement(SELECT_CONTEXT);
                if (entityIdentifier != null) {
                    cstmt.setString(1, databaseIdentifier);
                    cstmt.setString(2, entityIdentifier);
                } else {
                    cstmt.setNull(1, -1);
                    cstmt.setNull(2, -2);
                }

                cstmt.execute();
            } finally {
                if (cstmt != null) {
                    cstmt.close();
                }

            }
        } catch (Exception var11) {
            this.releaseAnyConnection(connection);
            throw new SQLException("Problemas ao definir tenante.", var11);
        }
    }

    public boolean isUnwrappableAs(Class unwrapType) {
        return MultiTenantConnectionProvider.class.equals(unwrapType) || AbstractMultiTenantConnectionProvider.class
                .isAssignableFrom(unwrapType);
    }

    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.close();
    }

    public boolean supportsAggressiveRelease() {
        return true;
    }

    public <T> T unwrap(Class<T> unwrapType) {
        if (this.isUnwrappableAs(unwrapType)) {
            return (T) this;
        } else {
            throw new UnknownUnwrapTypeException(unwrapType);
        }
    }
}
