package tutorial.trucksDispatchGuiceMybatis.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class DatabaseUpdateUnitTest {

    @Mock
    private DatabaseUpdater.ConnectionProvider connectionProvider;

    @Mock
    private DatabaseUpdater.DatabaseProvider databaseProvider;

    @Mock
    private DatabaseUpdater.LiquibaseProvider liquibaseProvider;

    @Mock
    private Liquibase liquibase;

    @Mock
    private Database database;

    @Mock
    private Connection connection;

    private AutoCloseable mocks;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void updateDatabase() throws Exception {
        final String url = "url-1";
        final String username = "username-2";
        final String password = "password-3";
        final String changeLogFile = "changeLogFile-4";

        doReturn(connection).when(connectionProvider).getConnection(any(), any(), any());
        doReturn(database).when(databaseProvider).getDatabase(any());
        doReturn(liquibase).when(liquibaseProvider).getLiquibase(any(), any());

        new DatabaseUpdater(
                url, username, password, changeLogFile,
                connectionProvider, databaseProvider, liquibaseProvider
        ).update();
        verify(connectionProvider).getConnection(url, username, password);
        verify(databaseProvider).getDatabase(any());
        ArgumentCaptor<String> changeLogFileCaptor = ArgumentCaptor.forClass(String.class);
        verify(liquibaseProvider).getLiquibase(any(), changeLogFileCaptor.capture());
        Assertions.assertEquals(changeLogFile, changeLogFileCaptor.getValue());
    }
}
