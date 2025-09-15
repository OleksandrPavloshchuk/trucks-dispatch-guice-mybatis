package tutorial.trucksDispatchGuiceMybatis.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public record DatabaseUpdater(
        String url,
        String username,
        String password,
        String changeLogFile,
        ConnectionProvider connectionProvider,
        DatabaseProvider databaseProvider,
        LiquibaseProvider liquibaseProvider
) {

    public static class ConnectionProvider {
        public Connection getConnection(String url, String username, String password) throws SQLException {
            return DriverManager.getConnection(url, username, password);
        }
    }

    public static class DatabaseProvider {
        public Database getDatabase(Connection connection) throws LiquibaseException {
            return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        }
    }

    public static class LiquibaseProvider {
        public Liquibase getLiquibase(Database database, String changeLogFile) {
            return new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
        }
    }

    public void update() throws LiquibaseException, SQLException {
        final Connection connection = connectionProvider.getConnection(url, username, password);
        final Database database = databaseProvider.getDatabase(connection);
        final Liquibase liquibase = liquibaseProvider.getLiquibase(database, changeLogFile);
        liquibase.update();
    }

    public static void main(String[] args) throws SQLException, LiquibaseException {
        final String url = "jdbc:postgresql://localhost:15432/trucksdispatch";
        final String username = "td";
        final String password = "tdpass";
        final String changeLogFile = "db/changelog/db.changelog-master.xml";

        new DatabaseUpdater(
                url, username, password, changeLogFile,
                new ConnectionProvider(), new DatabaseProvider(), new LiquibaseProvider()
        ).update();
    }

}

