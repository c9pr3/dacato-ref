package cs.ecs.jdaoref;

import co.ecso.jdao.cache.Cache;
import co.ecso.jdao.cache.CachingConnectionWrapper;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * CachedDatabaseConnection.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 28.08.16
 */
public final class CachedDatabaseConnection extends CachingConnectionWrapper {
    private static Cache<CacheKey<?>, CompletableFuture<?>> CACHE = new ApplicationCache();

    public CachedDatabaseConnection(final ApplicationConfig config) throws SQLException {
        super(config, CACHE);
    }

}
