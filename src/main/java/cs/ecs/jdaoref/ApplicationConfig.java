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
    static final Cache<CacheKey, CompletableFuture<?>> CACHE = new ApplicationCache<>();


    @Override
    public String databasePoolName() {
        return "hsqlpool";
    }

    @Override
    public int databasePoolMin() {
        return 1;
    }

    @Override
    public int databasePoolMax() {
        return 10;
    }

    @Override
    public int databasePoolMaxSize() {
        return 100;
    }

    @Override
    public long databasePoolIdleTimeout() {
        return 1000;
    }

    @Override
    public String connectionString() {
        return "jdbc:hsqldb:mem:jdaoref";
    }

    @Override
    public synchronized ScheduledExecutorService threadPool() {
        //noinspection SynchronizeOnNonFinalField
        synchronized (threadFactoryBuilder) {
            if (threadFactoryBuilder == null) {
                threadFactoryBuilder = new ThreadFactoryBuilder()
                        .setNameFormat(this.databasePoolName() + "-%d").build();
            }
            return Executors.newSingleThreadScheduledExecutor(threadFactoryBuilder);
        }
    }

    @Override
    public ConnectionPool<Connection> databaseConnectionPool() {
        //noinspection SynchronizeOnNonFinalField
        synchronized (connectionPool) {
            if (connectionPool == null) {
                connectionPool = new snaq.db.ConnectionPool(databasePoolName(), databasePoolMin(),
                        databasePoolMax(), databasePoolMaxSize(), databasePoolIdleTimeout(),
                        connectionString(), null);
                ConnectionPoolManager.registerGlobalShutdownHook();
            }
            return () -> {
                final Connection connection = connectionPool.getConnection(databasePoolIdleTimeout());
                if (connection == null) {
                    throw new SQLException("Could not get connection from pool");
                }
                return connection;
            };
        }
    }
}
