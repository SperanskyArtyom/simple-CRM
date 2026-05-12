package ru.shift.demo.simple_crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.shift.demo.simple_crm.config.TestContainerConfig;

@SpringBootTest
@Import(TestContainerConfig.class)
class SimpleCrmApplicationTests {

	@Test
	void contextLoads() {
	}

}
