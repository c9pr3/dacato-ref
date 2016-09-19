package cs.ecs.jdaoref;

import co.ecso.jdao.database.internals.EntityFinder;
import co.ecso.jdao.database.internals.StatementFiller;
import co.ecso.jdao.database.query.DatabaseResultField;
import co.ecso.jdao.database.query.SingleColumnQuery;

import java.util.concurrent.CompletableFuture;

/**
 * MyEntityFinder.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 19.09.16
 */
final class MyEntityFinder implements EntityFinder {

    @Override
    public ApplicationConfig config() {
        return new ApplicationConfig();
    }

    @Override
    public StatementFiller statementFiller() {
        return new MyStatementFiller();
    }

    @Override
    public <S, W> CompletableFuture<DatabaseResultField<S>> findOne(final SingleColumnQuery<S, W> query) {
        return EntityFinder.super.findOne(query);
    }
}
