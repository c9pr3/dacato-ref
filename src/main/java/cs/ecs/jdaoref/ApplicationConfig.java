package cs.ecs.jdaoref;

import co.ecso.jdao.connection.ConnectionPool;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import snaq.db.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * ApplicationConfig.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 29.08.16
 */
public final class ApplicationConfig implements co.ecso.jdao.config.ApplicationConfig {

    private static snaq.db.ConnectionPool CONNECTION_POOL;
    private static ThreadFactory THREAD_FACTORY_BUILDER;

    @Override
    public String getMysqlHost() {
        return "localhost";
    }

    @Override
    public int getMysqlPort() {
        return 3306;
    }

    @Override
    public int getMaxConnections() {
        return 20;
    }

    @Override
    public String getPoolName() {
        return "hsqlpool";
    }

    @Override
    public int getMinPoolSize() {
        return 1;
    }

    @Override
    public int getMaxPoolSize() {
        return 10;
    }

    @Override
    public int getPoolMaxSize() {
        return 100;
    }

    @Override
    public long getPoolIdleTimeout() {
        return 1000;
    }

    @Override
    public String getConnectString() {
        return "jdbc:hsqldb:mem:jdaoref";
    }

    @Override
    public ScheduledExecutorService getThreadPool() {
        if (THREAD_FACTORY_BUILDER == null) {
            THREAD_FACTORY_BUILDER = new ThreadFactoryBuilder()
                    .setNameFormat(this.getPoolName() + "-%d").build();
        }
        return Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY_BUILDER);
    }

    @Override
    public ConnectionPool<Connection> getConnectionPool() {
        if (CONNECTION_POOL == null) {
            CONNECTION_POOL = new snaq.db.ConnectionPool(getPoolName(), getMinPoolSize(),
                    getMaxPoolSize(), getPoolMaxSize(), getPoolIdleTimeout(),
                    getConnectString(), null);
            ConnectionPoolManager.registerGlobalShutdownHook();
        }
        return () -> {
            final Connection connection = CONNECTION_POOL.getConnection(getPoolIdleTimeout());
            if (connection == null) {
                throw new SQLException("Could not get connection from pool");
            }
            return connection;
        };
    }
}
