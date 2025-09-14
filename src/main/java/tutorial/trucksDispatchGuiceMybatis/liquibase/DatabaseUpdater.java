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

public class DatabaseUpdater {
    public static void main(String[] args) throws SQLException, LiquibaseException {
        String url = "jdbc:postgresql://localhost:15432/trucksdispatch";
        String username = "td";
        String password = "tdpass";
        String changeLogFile = "db/changelog/db.changelog-master.xml";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update("");

            System.out.println("Liquibase update completed successfully!");
        }
    }
}

