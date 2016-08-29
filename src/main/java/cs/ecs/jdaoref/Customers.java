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
}
