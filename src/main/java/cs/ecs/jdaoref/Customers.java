package cs.ecs.dacatoref;

import co.ecso.dacato.database.ColumnList;
import co.ecso.dacato.database.DatabaseTable;
import co.ecso.dacato.database.internals.EntityFinder;
import co.ecso.dacato.database.internals.Inserter;
import co.ecso.dacato.database.internals.Truncater;
import co.ecso.dacato.database.query.DatabaseField;
import co.ecso.dacato.database.query.InsertQuery;
import co.ecso.dacato.database.query.MultiColumnQuery;
import co.ecso.dacato.database.query.SingleColumnQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Customers.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 15.03.16
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class Customers implements DatabaseTable<Long, Customer> {

    private final RefApplicationConfig config;

    /**
     * Construct.
     *
     * @param config Config.
     */
    public Customers(final RefApplicationConfig config) {
        this.config = config;
    }

    /**
     * Remove all entries.
     *
     * @return True if truncating succeeded.
     */
    CompletableFuture<Boolean> removeAll() {
        return truncate("TRUNCATE TABLE customer");
    }

    /**
     * Find one by id.
     *
     * @param id ID
     * @return Found entity.
     */
    @Override
    public CompletableFuture<Customer> findOne(final Long id) {
        return this.findOne(new SingleColumnQuery<>("SELECT %s FROM customer WHERE %s = ?", Customer.Fields.ID,
                Customer.Fields.ID, id)).thenApply(foundId -> new Customer(config, foundId.resultValue()));
    }

    final CompletableFuture<Customer> findOneByFirstName(final String firstName) {
        final SingleColumnQuery<Long, String> query = new SingleColumnQuery<>("SELECT %s FROM customer WHERE %s = ?",
                Customer.Fields.ID,
                Customer.Fields.FIRST_NAME, firstName);
        return this.findOne(query).thenApply(foundId -> new Customer(config, foundId.resultValue()));
    }

    public CompletableFuture<Customer> findOneByFirstNameAndLastName(final String firstName, final String lastName) {
        Map<DatabaseField<?>, Object> m = new HashMap<>();
        m.put(Customer.Fields.FIRST_NAME, firstName);
        m.put(Customer.Fields.LAST_NAME, lastName);
        final ColumnList values = () -> m;
        return findOne(new MultiColumnQuery<>("SELECT %s FROM customer WHERE %s = ? AND %s = ?",
                Customer.Fields.ID, values)).thenApply(id -> new Customer(config, id.resultValue()));
    }

    /**
     * Add a new entity.
     *
     * @param customerFirstName First Name.
     * @param customerLastName  Last Name.
     * @param customerNumber    Number.
     * @return Newly created entity.
     */
    public CompletableFuture<Customer> add(final String customerFirstName, final String customerLastName,
                                           final long customerNumber) {
        final InsertQuery<Long> query = new InsertQuery<>("INSERT INTO customer (%s, %s, %s, %s) " +
                "VALUES (null, ?, ?, ?)", Customer.Fields.ID);
        query.add(Customer.Fields.FIRST_NAME, customerFirstName);
        query.add(Customer.Fields.LAST_NAME, customerLastName);
        query.add(Customer.Fields.NUMBER, customerNumber);
        return add(query).thenApply(id -> new Customer(config, id.resultValue()));
    }

    // Just to show it is possible
    @Override
    public Truncater truncater() {
        return new MyTruncater();
    }

    // Just to show it is possible
    @Override
    public Inserter<Long> inserter() {
        return new MyInserter<>();
    }

    // Just to show it is possible
    @Override
    public EntityFinder entityFinder() {
        return new MyEntityFinder();
    }

    /**
     * Find all entities.
     *
     * @return List of entities found.
     */
    @Override
    public CompletableFuture<List<Customer>> findAll() {
        return this.findMany(new SingleColumnQuery<>("SELECT %s FROM customer", Customer.Fields.ID))
                .thenApply(list -> list.stream().map(foundId -> new Customer(config, foundId.resultValue()))
                        .collect(Collectors.toList()));
    }

    @Override
    public co.ecso.dacato.config.ApplicationConfig config() {
        return config;
    }
}
