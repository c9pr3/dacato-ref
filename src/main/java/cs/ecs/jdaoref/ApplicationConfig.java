package cs.ecs.jdaoref;

import co.ecso.jdao.connection.ConnectionPool;
import co.ecso.jdao.database.cache.Cache;
import co.ecso.jdao.database.cache.CacheKey;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import snaq.db.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
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
final class ApplicationConfig implements co.ecso.jdao.config.ApplicationConfig {

    private static snaq.db.ConnectionPool connectionPool;
    private static ThreadFactory threadFactoryBuilder;
    static final Cache<CacheKey<?>, CompletableFuture<?>> CACHE = new ApplicationCache<>();

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
    public synchronized ScheduledExecutorService getThreadPool() {
        //noinspection SynchronizeOnNonFinalField
        synchronized (threadFactoryBuilder) {
            if (threadFactoryBuilder == null) {
                threadFactoryBuilder = new ThreadFactoryBuilder()
                        .setNameFormat(this.getPoolName() + "-%d").build();
            }
            return Executors.newSingleThreadScheduledExecutor(threadFactoryBuilder);
        }
    }

    @Override
    public ConnectionPool<Connection> getConnectionPool() {
        //noinspection SynchronizeOnNonFinalField
        synchronized (connectionPool) {
            if (connectionPool == null) {
                connectionPool = new snaq.db.ConnectionPool(getPoolName(), getMinPoolSize(),
                        getMaxPoolSize(), getPoolMaxSize(), getPoolIdleTimeout(),
                        getConnectString(), null);
                ConnectionPoolManager.registerGlobalShutdownHook();
            }
            return () -> {
                final Connection connection = connectionPool.getConnection(getPoolIdleTimeout());
                if (connection == null) {
                    throw new SQLException("Could not get connection from pool");
                }
                return connection;
            };
        }
    }
}
