package cs.ecs.dacatoref;

import co.ecso.dacato.database.internals.Inserter;
import co.ecso.dacato.database.internals.StatementFiller;
import co.ecso.dacato.database.query.DatabaseField;
import co.ecso.dacato.database.query.DatabaseResultField;
import co.ecso.dacato.database.query.InsertQuery;

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
final class MyInserter<T> implements Inserter<T> {

    @Override
    public RefApplicationConfig config() {
        return new RefApplicationConfig();
    }

    @Override
    public StatementFiller statementFiller() {
        return new MyStatementFiller();
    }

    @Override
    public CompletableFuture<DatabaseResultField<T>> add(final InsertQuery<T> query) {
        return Inserter.super.add(query);
    }

    @Override
    public DatabaseResultField<T> getResult(final String finalQuery,
                                            final DatabaseField<T> columnToSelect,
                                            final PreparedStatement stmt,
                                            final boolean returnGeneratedKey) throws SQLException {
        return Inserter.super.getResult(finalQuery, columnToSelect, stmt, returnGeneratedKey);
    }
}
