package cs.ecs.dacatoref;

import co.ecso.dacato.database.internals.EntityFinder;
import co.ecso.dacato.database.internals.StatementFiller;

/**
 * MyEntityFinder.
 *
 * @author Christian Senkowski (cs@2scale.net)
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

}
