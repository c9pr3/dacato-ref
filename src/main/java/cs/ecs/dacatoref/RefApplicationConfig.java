package cs.ecs.dacatoref;

import co.ecso.dacato.connection.ConnectionPool;
import co.ecso.dacato.database.cache.Cache;
import co.ecso.dacato.database.cache.CacheKey;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import snaq.db.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RefApplicationConfig.
 *
 * @author Christian Scharmach (cs@e-cs.co)
 * @version $Id:$
 * @since 29.08.16
 */
final class RefApplicationConfig implements co.ecso.dacato.config.ApplicationConfig {

    private static volatile snaq.db.ConnectionPool connectionPool;
    static final Cache CACHE = new ApplicationCache();
    private static volatile ExecutorService threadPool;

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
        return "jdbc:hsqldb:mem:dacatoref";
    }

    @Override
    public ExecutorService threadPool() {
        if (threadPool == null) {
            threadPool = Executors.newCachedThreadPool(new ThreadFactoryBuilder()
                    .setNameFormat(this.databasePoolName() + "-%d").build());
        }
        return threadPool;
    }

    @Override
    public ConnectionPool<Connection> databaseConnectionPool() {
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
