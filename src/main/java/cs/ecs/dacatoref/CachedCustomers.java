package cs.ecs.dacatoref;

import co.ecso.dacato.config.ApplicationConfig;
import co.ecso.dacato.database.CachedDatabaseTable;
import co.ecso.dacato.database.cache.Cache;
import co.ecso.dacato.database.query.Truncater;
import co.ecso.dacato.database.querywrapper.InsertQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * CachedCustomers.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 17.09.16
 */
@SuppressWarnings("unused")
final class CachedCustomers implements CachedDatabaseTable<Long, Customer> {

    private final ApplicationConfig config;

    public CachedCustomers(final ApplicationConfig config) {
        this.config = config;
    }

    public CompletableFuture<CachedCustomer> create(final String firstName, final String lastName, final long number) {
        final InsertQuery<Long> query = new InsertQuery<>(
                "INSERT INTO customer (%s, %s, %s, %s) VALUES (null, ?, ?, ?)", CachedCustomer.Fields.ID);
        query.add(CachedCustomer.Fields.FIRST_NAME, firstName);
        query.add(CachedCustomer.Fields.LAST_NAME, lastName);
        query.add(CachedCustomer.Fields.NUMBER, number);
        return this.add(query).thenApply(newId -> new CachedCustomer(config, newId.resultValue()));
    }

    @Override
    public ApplicationConfig config() {
        return config;
    }

    @Override
    public CompletableFuture<Customer> findOne(final Long id) {
        return null;
    }

    @Override
    public CompletableFuture<List<Customer>> findAll() {
        return null;
    }

    @Override
    public Truncater truncater() {
        return new MyTruncater();
    }

    @Override
    public Cache cache() {
        return RefApplicationConfig.CACHE;
    }

}
