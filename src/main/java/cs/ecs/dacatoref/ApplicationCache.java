package cs.ecs.dacatoref;

import co.ecso.dacato.database.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * ApplicationCache.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 04.09.16
 */
final class ApplicationCache<K, V> implements Cache<K, V> {
    private final com.google.common.cache.Cache<K, V>
            cache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).maximumSize(50).build();

    @Override
    public V getIfPresent(final K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public V get(final K var1, final Callable<? extends V> var2) throws ExecutionException {
        return cache.get(var1, var2);
    }

    @Override
    public Map<K, V> getAllPresent(final Iterable<?> var1) {
        return cache.getAllPresent(var1);
    }

    @Override
    public void put(final K var1, final V var2) {
        cache.put(var1, var2);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> var1) {
        cache.putAll(var1);
    }

    @Override
    public void invalidate(final Object var1) {
        cache.invalidate(var1);
    }

    @Override
    public void invalidateAll(final Iterable<K> var1) {
        cache.invalidateAll(var1);
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public void cleanUp() {
        cache.cleanUp();
    }
}
