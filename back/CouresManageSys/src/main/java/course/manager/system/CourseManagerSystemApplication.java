package course.manager.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan({"course.manager.system.dao.mapper"})
@EnableCaching
public class CourseManagerSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseManagerSystemApplication.class, args);
    }
}
