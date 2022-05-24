package comfoxminded.aprihodko.misc;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.impl.DaoTestBaseClass;
import com.foxminded.aprihodko.dao.impl.StudentsDaoImpl;
import com.foxminded.aprihodko.misc.GenerateStudents;
import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

class GenerateStudentsTest extends DaoTestBaseClass {

    StudentDao dao = new StudentsDaoImpl();
    GenerateStudents generateStudents = new GenerateStudents(dao);

    @Test
    void shouldGenerateNStudents() throws SQLException {
        inTransaction(datasource, connection -> {
            int N = 200;
            generateStudents.generateData(connection, N);
            try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM school.students")) {
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    assertEquals(N, rs.getInt(1));
                }
            }
        });
    }

    @Test
    void shouldBeExceptionInTransaction() throws SQLException {
        inTransaction(datasource, connection -> {
            int countOfGroup = 1200;
            Exception e = assertThrows(SQLException.class, () -> inTransaction(datasource,
                    connections -> generateStudents.generateData(connection, countOfGroup)));
            assertEquals("Exception in transaction", e.getMessage());
        });
    }
}
