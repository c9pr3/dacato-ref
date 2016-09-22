package cs.ecs.jdaoref;

import co.ecso.jdao.database.internals.Truncater;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.CompletableFuture;

/**
 * MyTruncater.
 *
 * Not needed, but we want to show it is possible.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 19.09.16
 */
final class MyTruncater implements Truncater {

    @Override
    public CompletableFuture<Boolean> truncate(final String query) {
        final CompletableFuture<Boolean> retValFuture = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try (final Connection c = config().databaseConnectionPool().getConnection()) {
                try (final PreparedStatement stmt = c.prepareStatement(query)) {
                    retValFuture.complete(stmt.execute());
                }
            } catch (final Exception e) {
                retValFuture.completeExceptionally(e);
            }
        }, config().threadPool());
        return retValFuture;
    }

    @Override
    public RefApplicationConfig config() {
        return new RefApplicationConfig();
    }
}
