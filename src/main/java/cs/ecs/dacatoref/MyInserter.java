package cs.ecs.dacatoref;

import co.ecso.dacato.database.query.Inserter;
import co.ecso.dacato.database.querywrapper.DatabaseField;
import co.ecso.dacato.database.querywrapper.DatabaseResultField;
import co.ecso.dacato.database.querywrapper.InsertQuery;
import co.ecso.dacato.database.statement.StatementFiller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * MyInserter.
 *
 * Not needed, but we want to show it is possible.
 *
 * @author Christian Scharmach (cs@e-cs.co)
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
    public int statementOptions() {
        return 0;
    }

    @Override
    public DatabaseResultField<T> getResult(final String finalQuery,
                                            final DatabaseField<T> columnToSelect,
                                            final PreparedStatement stmt,
                                            final boolean returnGeneratedKey) throws SQLException {
        return Inserter.super.getResult(finalQuery, columnToSelect, stmt, returnGeneratedKey);
    }
}
