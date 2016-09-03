package cs.ecs.jdaoref;

import co.ecso.jdao.Inserter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * FailingInserter.
 *
 * We want our own insert mechanism as we already know the config.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 29.08.16
 */
interface FailingInserter<T> extends Inserter<T> {

    @Override
    default T getResult(final String query, final PreparedStatement stmt) throws SQLException {
        throw new SQLException("foo");
    }
}
