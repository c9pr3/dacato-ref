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

    /**
     * Construct.
     *
     * @param config Config.
     */
    public Customers(final ApplicationConfig config) {
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
    CompletableFuture<Customer> findOne(final CompletableFuture<Long> id) {
        return find(new SingleFindQuery<>("SELECT %s FROM customer WHERE %s = ?",
                Customer.Fields.ID, ColumnList.build(Customer.Fields.ID, id)))
                .thenApply(id1 -> new Customer(config, id1));
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
        final Map<DatabaseField<?>, Object> map = new LinkedHashMap<>();
        return insert("INSERT INTO customer VALUES (null, ?, ?, ?)",
                new ColumnList().keys(Customer.Fields.FIRST_NAME, Customer.Fields.LAST_NAME, Customer.Fields.NUMBER)
                        .values(customerFirstName, customerLastName, customerNumber).build()
        ).thenApply(id -> new Customer(config, id));
    }

    /**
     * Find all entities.
     *
     * @return List of entities found.
     */
    public CompletableFuture<List<Customer>> findAll() {
        return find(new ListFindQuery<>("SELECT %s FROM customer", Customer.Fields.ID))
                .thenApply(longList -> longList.stream().map(l -> new Customer(config, l)).collect(Collectors.toList()));
    }

    /**
     * Only find id and first name by id. Showoff.
     *
     * @param id        ID to find.
     * @param firstName FirstName to find.
     * @return List of lists containing id and firstname.
     */
    public CompletableFuture<List<List<?>>> findIdAndFirstNameByID(final CompletableFuture<Long> id,
                                                                   final CompletableFuture<String> firstName) {
        final List<DatabaseField<?>> columnsToSelect = new LinkedList<>();
        columnsToSelect.add(Customer.Fields.ID);
        columnsToSelect.add(Customer.Fields.FIRST_NAME);
        columnsToSelect.add(Customer.Fields.LAST_NAME);

        final Map<DatabaseField<?>, CompletableFuture<?>> columnsWhere = new HashMap<>();
        columnsWhere.put(Customer.Fields.ID, id);
        columnsWhere.put(Customer.Fields.FIRST_NAME, firstName);

        MultipleFindQuery query = new MultipleFindQuery(
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
