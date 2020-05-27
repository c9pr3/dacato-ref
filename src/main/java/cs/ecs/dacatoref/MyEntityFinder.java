package cs.ecs.dacatoref;

import co.ecso.dacato.database.query.EntityFinder;
import co.ecso.dacato.database.statement.StatementFiller;

/**
 * MyEntityFinder.
 *
 * @author Christian Scharmach (cs@e-cs.co)
 * @version $Id:$
 * @since 19.09.16
 */
final class MyEntityFinder implements EntityFinder {

    @Override
    public RefApplicationConfig config() {
        return new RefApplicationConfig();
    }

    @Override
    public StatementFiller statementFiller() {
        return new MyStatementFiller();
    }

    @Override
    public int statementOptions() {
        return 0;
    }

}
