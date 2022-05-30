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

    public static final int COURSE_COUNT = 10;
    public static final int COUNT_OF_STUDENTS = 10;
    public static final int MIN_COURSE_COUNT = 3;
    public static final int MAX_COURSE_COUNT = 5;

    @Test
    void shouldMapStudentsToCourses() throws SQLException {
        CourseDao courseDao = new CourseDaoImpl();
        StudentDao studentDao = new StudentsDaoImpl();
        inTransaction(datasource, connection -> {
            new GenerateCourses(courseDao).generateDate(connection, COURSE_COUNT);
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
        inTransaction(datasource, connection -> {
            new GenerateCourses(courseDao).generateDate(connection, 1);
            new StudentCoursesMappingGenerator(studentDao, courseDao).mapStudentsToCourses(connection, COURSE_COUNT,
                    COUNT_OF_STUDENTS);
        });
        
        inTransaction(datasource, connetction ->{
            StudentCoursesMappingGenerator mappingGenerator = new StudentCoursesMappingGenerator(studentDao, courseDao);
            Exception e = assertThrows(RuntimeException.class,() -> inTransaction(datasource, connection -> mappingGenerator.mapStudentsToCourses(connection, 50, 100)));
            assertEquals("", e.getMessage());
        });
    }
}
