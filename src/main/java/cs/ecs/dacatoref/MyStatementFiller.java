package cs.ecs.dacatoref;

import co.ecso.dacato.database.querywrapper.DatabaseField;
import co.ecso.dacato.database.statement.StatementFiller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * MyStatementFiller.
 *
 * Not needed, but we want to show it is possible.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 19.09.16
 */
final class MyStatementFiller implements StatementFiller {

    @Override
    public PreparedStatement fillStatement(final String query, final List<DatabaseField<?>> columnsWhere,
                                           final List<?> valuesWhere,
                                           final PreparedStatement stmt) throws SQLException {
        for (int i = 0; i < valuesWhere.size(); i++) {
            final Object valueToSet = valuesWhere.get(i);
            if (columnsWhere.get(i) == null) {
                // it may happen if no where is set
                continue;
            }
            final int sqlType = columnsWhere.get(i).sqlType();
            try {
                stmt.setObject(i + 1, valueToSet, sqlType);
            } catch (final SQLException e) {
                throw new SQLException(String.format("Could not set %s (%s) to %d: %s", valueToSet,
                        valueToSet.getClass().getSimpleName(), sqlType, e));
            }
        }
        return stmt;
    }

}
