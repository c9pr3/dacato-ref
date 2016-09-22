package cs.ecs.jdaoref;

import co.ecso.jdao.database.ColumnList;
import co.ecso.jdao.database.DatabaseEntity;
import co.ecso.jdao.database.query.DatabaseField;
import co.ecso.jdao.database.query.DatabaseResultField;
import co.ecso.jdao.database.query.SingleColumnQuery;
import co.ecso.jdao.database.query.SingleColumnUpdateQuery;

import java.sql.Types;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Customer.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 29.08.16
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class Customer implements DatabaseEntity<Long> {
    private static final String TABLE_NAME = "customer";
    private static final String QUERY = String.format("SELECT %%s FROM %s WHERE %%s = ?", TABLE_NAME);
    private final Long id;
    private final RefApplicationConfig config;
    private final AtomicBoolean objectValid = new AtomicBoolean(true);

    /**
     * Construct.
     *
     * @param config Config.
     * @param id     ID.
     */
    public Customer(final RefApplicationConfig config, final long id) {
        this.id = id;
        this.config = config;
    }

    @Override
    public Long primaryKey() {
        return this.id;
    }

    /**
     * Get first name.
     *
     * @return first name.
     */
    public CompletableFuture<DatabaseResultField<String>> firstName() {
        return this.findOne(new SingleColumnQuery<>(QUERY, Fields.FIRST_NAME, Fields.ID, this.primaryKey()), () ->
                this.objectValid);
    }

    /**
     * Get last name.
     *
     * @return last name.
     */
    public CompletableFuture<DatabaseResultField<String>> lastName() {
        return this.findOne(new SingleColumnQuery<>(QUERY, Fields.LAST_NAME, Fields.ID, this.primaryKey()), () ->
                this.objectValid);
    }

    /**
     * Get number.
     *
     * @return number.
     */
    public CompletableFuture<DatabaseResultField<Long>> number() {
        return this.findOne(new SingleColumnQuery<>(QUERY, Fields.NUMBER, Fields.ID, this.primaryKey()), () ->
                this.objectValid);
    }

    @Override
    public CompletableFuture<DatabaseEntity<Long>> save(ColumnList columnValuesToSet) {
        final SingleColumnUpdateQuery<Long> query = new SingleColumnUpdateQuery<>(
                "UPDATE customer SET %s WHERE %%s = ?", Fields.ID, id, columnValuesToSet);
        final CompletableFuture<Integer> updated = this.update(query, () -> this.objectValid);
        this.objectValid.set(false);
        return updated.thenApply(l -> new Customer(config, id));
    }

    @Override
    public co.ecso.jdao.config.ApplicationConfig config() {
        return this.config;
    }

    static final class Fields {
        static final DatabaseField<Long> ID = new DatabaseField<>("id", Long.class, Types.BIGINT);
        static final DatabaseField<Long> NUMBER = new DatabaseField<>("customer_number", Long.class, Types.BIGINT);
        static final DatabaseField<String> FIRST_NAME =
                new DatabaseField<>("customer_first_name", String.class, Types.VARCHAR);
        static final DatabaseField<String> LAST_NAME =
                new DatabaseField<>("customer_last_name", String.class, Types.VARCHAR);
        /**
         * Private constructor.
         */
        private Fields() {
            //unused
        }
    }
}
