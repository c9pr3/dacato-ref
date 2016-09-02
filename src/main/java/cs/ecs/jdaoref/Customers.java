package cs.ecs.jdaoref;

import co.ecso.jdao.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Customers.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 15.03.16
 */
public final class Customers {

    private final ApplicationConfig config;

    public Customers(final ApplicationConfig config) {
        Objects.requireNonNull(config);
        this.config = config;
    }

    CompletableFuture<Boolean> removeAll() {
        return ((Truncater) () -> config).truncate(new Query("TRUNCATE TABLE customer"));
    }

    CompletableFuture<Customer> findOne(final CompletableFuture<Long> id) {
        return ((Finder<Long>) () -> config).findOne(new Query("SELECT %s FROM customer WHERE id = ?"),
                Customer.Fields.ID, id).thenApply(id1 -> new Customer(config, id1));
    }

    public CompletableFuture<Customer> add(final String customerFirstName, final String customerLastName,
                                           final long customerNumber) {
        final Map<DatabaseField<?>, Object> map = new LinkedHashMap<>();
        map.put(Customer.Fields.FIRST_NAME, customerFirstName);
        map.put(Customer.Fields.LAST_NAME, customerLastName);
        map.put(Customer.Fields.NUMBER, customerNumber);
        return ((Inserter<Long>) () -> config)
                .insert(new Query("INSERT INTO customer VALUES (null, ?, ?, ?)"), map)
                .thenApply(id -> new Customer(config, id));
    }

    public CompletableFuture<List<Customer>> findAll() {
        return ((Finder<Long>) () -> config).findMany(new Query("SELECT %s FROM customer"),
                Customer.Fields.ID, new HashMap<>())
                .thenApply(longList -> longList.stream().map(l -> new Customer(config, l)).collect(Collectors.toList()));
    }

    public CompletableFuture<List<?>> findIdAndFirstNameByID(final CompletableFuture<Long> id,
                                                             final CompletableFuture<String> firstName) {

        final Query query = new Query("SELECT %s, %s, %s FROM customer WHERE %s = ? AND %s = ?");

        final List<DatabaseField<?>> columnsToReturn = new LinkedList<>();
        columnsToReturn.add(Customer.Fields.ID);
        columnsToReturn.add(Customer.Fields.FIRST_NAME);
        columnsToReturn.add(Customer.Fields.LAST_NAME);

        final Map<DatabaseField<?>, CompletableFuture<?>> columnsToSelect = new HashMap<>();
        columnsToSelect.put(Customer.Fields.ID, id);
        columnsToSelect.put(Customer.Fields.FIRST_NAME, firstName);

        return ((MultipleReturnFinder<?>) cs.ecs.jdaoref.ApplicationConfig::new)
                .findMultiple(query, columnsToReturn, columnsToSelect);
    }
}
