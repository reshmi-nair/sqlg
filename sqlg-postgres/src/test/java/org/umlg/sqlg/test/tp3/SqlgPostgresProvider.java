package org.umlg.sqlg.test.tp3;

import com.tinkerpop.gremlin.AbstractGraphProvider;
import com.tinkerpop.gremlin.structure.Graph;
import org.apache.commons.configuration.Configuration;
import org.umlg.sqlg.sql.dialect.SqlDialect;
import org.umlg.sqlg.structure.SqlG;
import org.umlg.sqlg.structure.SqlgDataSource;

import java.beans.PropertyVetoException;
import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2014/07/13
 * Time: 5:57 PM
 */
public class SqlgPostgresProvider extends AbstractGraphProvider {

    @Override
    public Map<String, Object> getBaseConfiguration(final String graphName, final Class<?> test, final String testMethodName) {
        return new HashMap<String, Object>() {{
            put("gremlin.graph", SqlG.class.getName());
            put("sql.dialect", "org.umlg.sqlg.sql.dialect.PostgresDialect");
            put("jdbc.url", "jdbc:postgresql://localhost:5432/" + graphName);
            put("jdbc.username", "postgres");
            put("jdbc.password", "postgres");
        }};
    }

    @Override
    public void clear(final Graph g, final Configuration configuration) throws Exception {
        if (null != g) {
            if (g.features().graph().supportsTransactions())
                g.tx().rollback();
            g.close();
        }
        SqlDialect sqlDialect;
        try {
            Class<?> sqlDialectClass = Class.forName(configuration.getString("sql.dialect"));
            Constructor<?> constructor = sqlDialectClass.getConstructor(Configuration.class);
            sqlDialect = (SqlDialect) constructor.newInstance(configuration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            SqlgDataSource.INSTANCE.setupDataSource(
                    sqlDialect.getJdbcDriver(),
                    configuration.getString("jdbc.url"),
                    configuration.getString("jdbc.username"),
                    configuration.getString("jdbc.password"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = SqlgDataSource.INSTANCE.get(configuration.getString("jdbc.url")).getConnection()) {
            DatabaseMetaData metadata = conn.getMetaData();
            if (sqlDialect.supportsCascade()) {
                String catalog = null;
                String schemaPattern = null;
                String tableNamePattern = "%";
                String[] types = {"TABLE"};
                ResultSet result = metadata.getTables(catalog, schemaPattern, tableNamePattern, types);
                while (result.next()) {
                    StringBuilder sql = new StringBuilder("DROP TABLE ");
                    sql.append(sqlDialect.maybeWrapInQoutes(result.getString(3)));
                    sql.append(" CASCADE");
                    if (sqlDialect.needsSemicolon()) {
                        sql.append(";");
                    }
                    try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}