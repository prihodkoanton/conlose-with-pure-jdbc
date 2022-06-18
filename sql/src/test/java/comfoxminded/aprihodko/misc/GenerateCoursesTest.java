package comfoxminded.aprihodko.misc;

import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.dao.impl.CourseDaoImpl;
import com.foxminded.aprihodko.dao.impl.DaoTestBaseClass;
import com.foxminded.aprihodko.misc.GenerateCourses;

class GenerateCoursesTest extends DaoTestBaseClass {

    private CourseDao dao = new CourseDaoImpl();
    private GenerateCourses generateCourses = new GenerateCourses(dao);

    @Test
    void shouldGenarateNCourses() throws SQLException {
        inTransaction(datasource, connection -> {
            int N = 10;
            generateCourses.generateDate(connection, N);
            try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM school.courses")) {
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
                    connections -> generateCourses.generateDate(connection, countOfGroup)));
            assertEquals("Exception in transaction", e.getMessage());
        });
    }
}
