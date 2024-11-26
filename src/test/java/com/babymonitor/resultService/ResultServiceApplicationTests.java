package com.babymonitor.resultService;

import com.babymonitor.resultService.model.Result;
import com.babymonitor.resultService.model.SimType;
import com.babymonitor.resultService.repository.ResultRepository;
import com.babymonitor.resultService.service.ResultServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest
@Import(ResultServiceImpl.class)
class ResultServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private ResultServiceImpl resultService;

	@Autowired
	private ResultRepository resultRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	void setUp() {
		resultRepository.deleteAll();
	}

	@Test
	void whenAddResult_thenResultIsPersistedSuccessfully() {
		// Given
		Result result = new Result("Test Result", 1, 123, SimType.TRAINING);

		// When
		resultService.addResult(result);

		// Then
		List<Result> savedResults = resultRepository.findByUser(1);
		assertThat(savedResults).hasSize(1);
		Result savedResult = savedResults.get(0);
		assertThat(savedResult.getResult()).isEqualTo("Test Result");
		assertThat(savedResult.getUser()).isEqualTo(1);
		assertThat(savedResult.getSession()).isEqualTo(123);
		assertThat(savedResult.getSimType()).isEqualTo(SimType.TRAINING);
	}

	@Test
	void whenFindByUser_thenReturnsCorrectResults() {
		// Given
		Result result1 = new Result("Result 1", 1, 1, SimType.TRAINING);
		Result result2 = new Result("Result 2", 1, 2, SimType.TRAINING);
		Result result3 = new Result("Result 3", 2, 3, SimType.EXAM);
		resultRepository.saveAll(List.of(result1, result2, result3));

		// When
		List<Result> userResults = resultService.findByUser(1);

		// Then
		assertThat(userResults).hasSize(2);
		assertThat(userResults)
				.extracting(Result::getResult)
				.containsExactlyInAnyOrder("Result 1", "Result 2");
	}

	@Test
	void whenFindResult_withValidId_thenReturnsCorrectResult() {
		// Given
		Result result = new Result("Test Result", 1, 123, SimType.EXAM);
		Result savedResult = resultRepository.save(result);

		// When
		Result foundResult = resultService.findByUserAndSession(1, 123);

		// Then
		assertNotNull(foundResult);
		assertThat(foundResult.getResult()).isEqualTo("Test Result");
		assertThat(foundResult.getUser()).isEqualTo(1);
		assertThat(foundResult.getSession()).isEqualTo(123);
		assertThat(foundResult.getSimType()).isEqualTo(SimType.EXAM);
	}

	@Test
	void whenFindResult_withInvalidId_thenReturnsNull() {
		// When
		Result result = resultService.findByUserAndSession(1, 999);

		// Then
		assertNull(result);
	}

	@Test
	void whenFindByUser_withNonExistentUser_thenReturnsEmptyList() {
		// When
		List<Result> results = resultService.findByUser(999);

		// Then
		assertThat(results).isEmpty();
	}

}
