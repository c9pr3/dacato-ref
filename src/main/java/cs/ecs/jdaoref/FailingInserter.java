package cs.ecs.jdaoref;

import co.ecso.jdao.ApplicationConfig;
import co.ecso.jdao.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * FailingInserter.
 *
 * We want our own insert mechanism as we already know the config.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 29.08.16
 */
public interface FailingInserter<T> extends co.ecso.jdao.Inserter<T> {

    @Override
    default void getResult(Query query, CompletableFuture<T> retValFuture, PreparedStatement stmt) throws SQLException {
        retValFuture.completeExceptionally(new Exception("foo"));
    }

}
