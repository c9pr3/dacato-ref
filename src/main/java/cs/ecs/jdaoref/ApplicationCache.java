package cs.ecs.jdaoref;

import co.ecso.jdao.database.cache.Cache;
import co.ecso.jdao.database.cache.CacheKey;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * ApplicationCache.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 04.09.16
 */
final class ApplicationCache implements Cache<CacheKey<?>, CompletableFuture<?>> {
    private static final com.google.common.cache.Cache<CacheKey<?>, CompletableFuture<?>>
            CACHE = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).maximumSize(50).build();

    @Override
    public CompletableFuture<?> getIfPresent(final CacheKey<?> key) {
        return CACHE.getIfPresent(key);
    }

    @Override
    public CompletableFuture<?> get(final CacheKey<?> var1,
                                    final Callable<? extends CompletableFuture<?>> var2) throws ExecutionException {
        return CACHE.get(var1, var2);
    }

    @Override
    public Map<CacheKey<?>, CompletableFuture<?>> getAllPresent(final Iterable<?> var1) {
        return CACHE.getAllPresent(var1);
    }

    @Override
    public void put(final CacheKey<?> var1, final CompletableFuture<?> var2) {
        CACHE.put(var1, var2);
    }

    @Override
    public void putAll(final Map<? extends CacheKey<?>, ? extends CompletableFuture<?>> var1) {
        CACHE.putAll(var1);
    }

    @Override
    public void invalidate(final Object var1) {
        CACHE.invalidate(var1);
    }

    @Override
    public void invalidateAll(final Iterable<?> var1) {
        CACHE.invalidateAll(var1);
    }

    @Override
    public void invalidateAll() {
        CACHE.invalidateAll();
    }

    @Override
    public long size() {
        return CACHE.size();
    }

    @Override
    public void cleanUp() {
        CACHE.cleanUp();
    }

}
