package cs.ecs.jdaoref;

import co.ecso.jdao.DatabaseEntity;
import co.ecso.jdao.DatabaseField;
import co.ecso.jdao.Finder;
import co.ecso.jdao.Query;

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
public final class Customer implements DatabaseEntity<Long> {
    private static final String TABLE_NAME = "customer";
    private static final Query QUERY = new Query(String.format("SELECT %%s FROM %s WHERE id = ?", TABLE_NAME));
    private final Long id;
    private final ApplicationConfig config;
    private AtomicBoolean invalid = new AtomicBoolean(false);

    public Customer(final ApplicationConfig config, final long id) {
        this.id = id;
        this.config = config;
    }

    @Override
    public Long id() {
        this.checkValidity();
        return this.id;
    }

    public CompletableFuture<String> firstName() {
        this.checkValidity();
        return ((Finder<String>) () -> config).findOne(QUERY, Fields.FIRST_NAME,
                CompletableFuture.completedFuture(this.id()));
    }

    public CompletableFuture<String> lastName() {
        this.checkValidity();
        return ((Finder<String>) () -> config).findOne(QUERY, Fields.LAST_NAME,
                CompletableFuture.completedFuture(this.id()));
    }

    public CompletableFuture<Long> number() {
        this.checkValidity();
        return ((Finder<Long>) () -> config).findOne(QUERY, Fields.NUMBER,
                CompletableFuture.completedFuture(this.id()));
    }

    @Override
    public CompletableFuture<? extends DatabaseEntity> save(Map<DatabaseField<?>, ?> map) {
        this.checkValidity();

        //TODO
        return null;
    }

    @Override
    public String toJson() throws SQLException {
        this.checkValidity();

        //TODO
        return "";
    }

    @Override
    public void checkValidity() {
        if (this.invalid.get()) {
            throw new RuntimeException("Object destroyed");
        }
    }

    static final class Fields {
        public static final DatabaseField<Long> ID = new DatabaseField<>("id", -1L, Types.BIGINT);
        public static final DatabaseField<Long> NUMBER = new DatabaseField<>("customer_number", -1L, Types.BIGINT);
        public static final DatabaseField<String> FIRST_NAME = new DatabaseField<>("customer_first_name", "", Types.VARCHAR);
        public static final DatabaseField<String> LAST_NAME = new DatabaseField<>("customer_last_name", "", Types.VARCHAR);
    }
}
