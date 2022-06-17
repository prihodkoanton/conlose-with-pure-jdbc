package comfoxminded.aprihodko.misc;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.foxminded.aprihodko.dao.CourseDao;
import com.foxminded.aprihodko.dao.StudentDao;
import com.foxminded.aprihodko.dao.impl.CourseDaoImpl;
import com.foxminded.aprihodko.dao.impl.DaoTestBaseClass;
import com.foxminded.aprihodko.dao.impl.StudentsDaoImpl;
import com.foxminded.aprihodko.misc.GenerateCourses;
import com.foxminded.aprihodko.misc.GenerateStudents;
import com.foxminded.aprihodko.misc.StudentCoursesMappingGenerator;
import com.foxminded.aprihodko.model.Course;
import com.foxminded.aprihodko.model.Students;

import static com.foxminded.aprihodko.utils.TransactionUtils.inTransaction;

class StudentCoursesMappingGeneratorTest extends DaoTestBaseClass {

    public static final int COUNT_OF_COURSE = 10;
    public static final int COUNT_OF_STUDENTS = 10;
    public static final int MIN_COURSE_COUNT = 3;
    public static final int MAX_COURSE_COUNT = 5;

    @Test
    void shouldMapStudentsToCourses() throws SQLException {
        CourseDao courseDao = new CourseDaoImpl();
        StudentDao studentDao = new StudentsDaoImpl();
        inTransaction(datasource, connection -> {
            new GenerateCourses(courseDao).generateDate(connection, COUNT_OF_COURSE);
            new GenerateStudents(studentDao).generateData(connection, COUNT_OF_STUDENTS);
            new StudentCoursesMappingGenerator(studentDao, courseDao).mapStudentsToCourses(connection, MIN_COURSE_COUNT,
                    MAX_COURSE_COUNT);
        });

        inTransaction(datasource, connection -> {
            List<Students> students = studentDao.findAll(connection);
            for (Students student : students) {
                List<Course> assignedCourses = courseDao.findByStudentId(connection, student.getId());
                Assertions.assertTrue(assignedCourses.size() >= MIN_COURSE_COUNT);
                Assertions.assertTrue(assignedCourses.size() < MAX_COURSE_COUNT);

            }
        });
    }

    @Test
    void shouldReturnException() throws SQLException {
        CourseDao courseDao = new CourseDaoImpl();
        StudentDao studentDao = new StudentsDaoImpl();

        inTransaction(datasource, connetction -> {
            new GenerateCourses(courseDao).generateDate(connetction, COUNT_OF_COURSE);
            new GenerateStudents(studentDao).generateData(connetction, COUNT_OF_STUDENTS);
            StudentCoursesMappingGenerator mappingGenerator = new StudentCoursesMappingGenerator(studentDao, courseDao);
            Exception e = assertThrows(SQLException.class, () -> inTransaction(datasource,
                    connection -> mappingGenerator.mapStudentsToCourses(connection, 50, 10)));
            assertEquals("Exception in transaction", e.getMessage());
        });
    }
}
