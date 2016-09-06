package cs.ecs.jdaoref;

import co.ecso.jdao.database.ColumnList;
import co.ecso.jdao.database.DatabaseEntity;
import co.ecso.jdao.database.DatabaseField;
import co.ecso.jdao.database.SingleFindQuery;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Customer.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 29.08.16
 */
@SuppressWarnings("WeakerAccess")
public final class Customer implements DatabaseEntity<Long> {
    private static final String TABLE_NAME = "customer";
    private static final String QUERY = String.format("SELECT %%s FROM %s WHERE %%s = ?", TABLE_NAME);
    private final Long id;
    private final ApplicationConfig config;
    private AtomicBoolean invalid = new AtomicBoolean(false);

    /**
     * Construct.
     *
     * @param config Config.
     * @param id ID.
     */
    public Customer(final ApplicationConfig config, final long id) {
        this.id = id;
        this.config = config;
    }

    @Override
    public Long id() {
        this.checkValidity();
        return this.id;
    }

    /**
     * Get first name.
     *
     * @return first name.
     */
    public CompletableFuture<String> firstName() {
        this.checkValidity();
        return this.findById(QUERY, Fields.FIRST_NAME, Fields.ID);
    }

    /**
     * Get last name.
     *
     * @return last name.
     */
    public CompletableFuture<String> lastName() {
        this.checkValidity();
        return this.findById(QUERY, Fields.LAST_NAME, Fields.ID);
    }

    /**
     * Get number.
     *
     * @return number.
     */
    public CompletableFuture<Long> number() {
        this.checkValidity();
        return this.findById(QUERY, Fields.NUMBER, Fields.ID);
    }

    @Override
    public CompletableFuture<? extends DatabaseEntity> save(final Map<DatabaseField<?>, ?> valuesMap,
                                                            final Map<DatabaseField<?>, ?> whereMap) {
        this.checkValidity();
        return this.update("UPDATE customer SET %s WHERE %s", valuesMap, whereMap)
                .thenCompose(rVal -> find(new SingleFindQuery<>(QUERY, Fields.ID,
                        ColumnList.build(Fields.ID, CompletableFuture.completedFuture(id))))
                        .thenApply(id1 -> new Customer(config, id1)));
    }

    @Override
    public String toJson() throws SQLException {
        this.checkValidity();
        return "{}";
    }

    @Override
    public void checkValidity() {
        if (this.invalid.get()) {
            throw new RuntimeException("Object destroyed");
        }
    }

    @Override
    public co.ecso.jdao.config.ApplicationConfig config() {
        return this.config;
    }

    static final class Fields {
        /**
         * Private constructor.
         */
        private Fields() {
            //unused
        }
        static final DatabaseField<Long> ID = new DatabaseField<>("id", -1L, Types.BIGINT);
        static final DatabaseField<Long> NUMBER = new DatabaseField<>("customer_number", -1L, Types.BIGINT);
        static final DatabaseField<String> FIRST_NAME = new DatabaseField<>("customer_first_name", "", Types.VARCHAR);
        static final DatabaseField<String> LAST_NAME = new DatabaseField<>("customer_last_name", "", Types.VARCHAR);
    }
}
