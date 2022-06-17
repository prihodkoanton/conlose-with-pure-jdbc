package comfoxminded.aprihodko.misc;

import static org.junit.jupiter.api.Assertions.*;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.foxminded.aprihodko.dao.GroupDao;
import com.foxminded.aprihodko.dao.impl.DaoTestBaseClass;
import com.foxminded.aprihodko.dao.impl.GroupDaoImpl;
import com.foxminded.aprihodko.misc.GeneratorGroups;

class GeneratorGroupsTest extends DaoTestBaseClass {

    private GroupDao dao = new GroupDaoImpl();
    private GeneratorGroups generatorGroups = new GeneratorGroups(dao);

    @Test
    void shouldGenarateNGroups() throws SQLException {
        inTransaction(datasource, connection -> {
            int N = 10;
            generatorGroups.generateDate(connection, N);
            try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM school.groups")) {
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    assertEquals(N, rs.getInt(1));
                }
            }
        });
    }

    @Test
    void shouldBeIllegalArgumentException_CountMustBeLessThen11() throws SQLException {
        inTransaction(datasource, connection -> {
            int countOfGroup = 11;
            Exception e = assertThrows(SQLException.class, () -> inTransaction(datasource,
                    connections -> generatorGroups.generateDate(connection, countOfGroup)));
            assertEquals("Exception in transaction", e.getMessage());
        });
    }
}
