package cs.ecs.jdaoref;

import co.ecso.jdao.database.*;

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
@SuppressWarnings("WeakerAccess")
public final class Customers implements DatabaseTable<Long> {

    private final ApplicationConfig config;

    public Customers(final ApplicationConfig config) {
        Objects.requireNonNull(config);
        this.config = config;
    }

    CompletableFuture<Boolean> removeAll() {
        return truncate("TRUNCATE TABLE customer");
    }

    CompletableFuture<Customer> findOne(final CompletableFuture<Long> id) {
        return find(new SingleFindQuery<>("SELECT %s FROM customer WHERE %s = ?",
                Customer.Fields.ID, ColumnList.build(Customer.Fields.ID, id)))
                .thenApply(id1 -> new Customer(config, id1));
    }

    public CompletableFuture<Customer> add(final String customerFirstName, final String customerLastName,
                                           final long customerNumber) {
        final Map<DatabaseField<?>, Object> map = new LinkedHashMap<>();
        map.put(Customer.Fields.FIRST_NAME, customerFirstName);
        map.put(Customer.Fields.LAST_NAME, customerLastName);
        map.put(Customer.Fields.NUMBER, customerNumber);
        return insert("INSERT INTO customer VALUES (null, ?, ?, ?)", map).thenApply(id -> new Customer(config, id));
    }

    public CompletableFuture<List<Customer>> findAll() {
        return find(new ListFindQuery<>("SELECT %s FROM customer", Customer.Fields.ID))
                .thenApply(longList -> longList.stream().map(l -> new Customer(config, l)).collect(Collectors.toList()));
    }

    public CompletableFuture<List<List<?>>> findIdAndFirstNameByID(final CompletableFuture<Long> id,
                                                             final CompletableFuture<String> firstName) {

        final List<DatabaseField<?>> columnsToSelect = new LinkedList<>();
        columnsToSelect.add(Customer.Fields.ID);
        columnsToSelect.add(Customer.Fields.FIRST_NAME);
        columnsToSelect.add(Customer.Fields.LAST_NAME);

        final Map<DatabaseField<?>, CompletableFuture<?>> columnsWhere = new HashMap<>();
        columnsWhere.put(Customer.Fields.ID, id);
        columnsWhere.put(Customer.Fields.FIRST_NAME, firstName);

        MultipleFindQuery query = new MultipleFindQuery (
                "SELECT %s, %s, %s FROM customer WHERE %s = ? AND %s = ?",
                columnsToSelect,
                columnsWhere
        );
        return find(query);
    }

    @Override
    public co.ecso.jdao.config.ApplicationConfig config() {
        return config;
    }
}
