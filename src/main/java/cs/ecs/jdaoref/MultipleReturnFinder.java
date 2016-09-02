package cs.ecs.jdaoref;

import co.ecso.jdao.DatabaseField;
import co.ecso.jdao.Finder;
import co.ecso.jdao.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * MultipleReturnFinder.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 30.08.16
 */
public interface MultipleReturnFinder<Void> extends Finder<Void> {

    default CompletableFuture<List<?>> findMultiple(final Query query, final List<DatabaseField<?>> columnsToReturn,
                                                    final Map<DatabaseField<?>, CompletableFuture<?>> columnsToSelect) {

        final CompletableFuture<List<?>> rval = new CompletableFuture<>();

        final String finalQuery = String.format(query.getQuery(), columnsToReturn.toArray());
        System.out.println("FINAL QUERY: " + finalQuery);

        CompletableFuture.runAsync(() -> {
            try (Connection c = config().getConnectionPool().getConnection()) {
                try (final PreparedStatement stmt = c.prepareStatement(finalQuery)) {
//                    fillStatement(columnsToSelect, stmt);
//                    getResult(query, columnToReturn, f, stmt);
                }
            } catch (final Exception e) {
                rval.completeExceptionally(e);
            }
        }, config().getThreadPool());

        return rval;
    }

//    default <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
//        CompletableFuture<?> allDoneFuture =
//                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
//        return allDoneFuture.thenApply(v ->
//                futures.stream().
//                        map(CompletableFuture::join).
//                        collect(Collectors.<T>toList())
//        );
//    }
}
