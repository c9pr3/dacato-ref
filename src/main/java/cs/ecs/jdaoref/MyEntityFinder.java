package cs.ecs.jdaoref;

import co.ecso.jdao.database.internals.EntityFinder;
import co.ecso.jdao.database.internals.StatementFiller;

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

}
