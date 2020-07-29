package horse.gargath.metricsexample;

import static com.jayway.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.junit.BeforeClass;
import com.jayway.restassured.RestAssured;


@SpringBootTest
public class FooTest {
    
    private static final int TEST_PORT = 8080;
    private static final String TEST_URL = "http://localhost";

    static {
        System.setProperty("jersey.config.test.container.port", String.valueOf(TEST_PORT)); // This isn't actually being honoured.
    }

    @BeforeClass
    public static void configureRestAssured() throws Exception
    {
        RestAssured.port = TEST_PORT;
        RestAssured.baseURI = TEST_URL;

        SpringApplication.run(App.class);
    }

    @Test
    public void shoudReturnCorrectFoo() {
        given().
        expect().
            body("id", equalTo(0)).
            statusCode(200).
        when().
            get("/foo/0");
    }
}