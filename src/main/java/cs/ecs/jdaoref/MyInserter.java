package cs.ecs.jdaoref;

import co.ecso.jdao.database.DatabaseEntity;
import co.ecso.jdao.database.internals.Inserter;
import co.ecso.jdao.database.internals.StatementFiller;
import co.ecso.jdao.database.query.DatabaseField;
import co.ecso.jdao.database.query.DatabaseResultField;
import co.ecso.jdao.database.query.InsertQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * MyInserter.
 *
 * Not needed, but we want to show it is possible.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 19.09.16
 */
final class MyInserter<T, R extends DatabaseEntity<T>> implements Inserter<T, R> {

    @Override
    public ApplicationConfig config() {
        return new ApplicationConfig();
    }

    @Override
    public StatementFiller statementFiller() {
        return new MyStatementFiller();
    }

    @Override
    public CompletableFuture<DatabaseResultField<T>> add(InsertQuery<T> query) {
        return Inserter.super.add(query);
    }

    @Override
    public DatabaseResultField<T> getResult(final String finalQuery, final DatabaseField<T> columnToSelect,
                                            final PreparedStatement stmt) throws SQLException {
        return Inserter.super.getResult(finalQuery, columnToSelect, stmt);
    }
}
