package cs.ecs.jdaoref;

import co.ecso.jdao.database.cache.Cache;
import co.ecso.jdao.database.cache.CacheKey;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * CachedDatabaseConnection.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 28.08.16
 */
@SuppressWarnings("unused")
public final class CachedDatabaseConnection {
    private static final Cache<CacheKey<?>, CompletableFuture<?>> CACHE = new ApplicationCache();

    /**
     * Construct.
     *
     * @param config Configuration.
     * @throws SQLException if connection could not be catched from pool
     */
    public CachedDatabaseConnection(final ApplicationConfig config) throws SQLException {
//        super(config, CACHE);
    }

}
