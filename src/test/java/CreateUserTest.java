import config.ApiConfig;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class CreateUserTest extends ApiConfig {

	/**
	 * 1. Делаем POST-запрос на создание user-а
	 * 2. Получаем ответ
	 * 3. Проверяем, что код ответа равен 200 ОК
	 */
	@Test
	public void createUserPositiveCase() {
		User user = new User(0, "Vanya", "Ivan", "Ivanov",
				"vivanov@post.com", "qwerty", "88005553555", 0);

		given()
				.spec(requestSpecification)
				.body(user)
				.when()
				.post("user")
				.then()
				.statusCode(200)
				.log()
				.body();
	}

	/**
	 * 1. Делаем  POST запрос на создание user-а c некорректным телом запроса
	 * 2. Сравниваем ответ со спецификацией
	 */
	@Test
	public void createUserNegativeCase() {
		String user = "";

		given()
				.spec(requestSpecification)
				.body(user)
				.when()
				.post("user")
				.then()
				.spec(responseSpecification);
	}

	/**
	 * 1. Делаем POST-запрос на создание user-а
	 * 2. Делаем GET-запрос по имени user-а
	 * 3. Сравниваем с телом ответа из json-файла
	 */
	@Test
	public void getUserTest() {
		User user = new User(78823426, "Vanya", "Ivan", "Ivanov",
				"vivanov@post.com", "qwerty", "88005553555", 0);

		given()
				.spec(requestSpecification)
				.body(user)
				.when()
				.post("user")
				.then()
				.statusCode(200);// создаем юзера

		Response response = given()
				.spec(requestSpecification)
				.when()
				.get("user/Vanya");//делаем GET-запрос по имени

		int userId = response.path("id");//достаем id из ответа, чтобы подложить в ожидаемый ответ

		try {
			JSONAssert.assertEquals(new String(Files.readAllBytes(Paths.get("src/test/resources/response.json")))
							.replaceAll("userId", String.valueOf(userId)),
					response.asString(), JSONCompareMode.NON_EXTENSIBLE);
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		//сравниваем ответ с json из файла с ресурасми, куда мы подставили id из актуального ответа
	}

	/**
	 * 1. Создаем юзера
	 * 2. Делаем запрос с некорректным именем
	 * 3. Сравниваем ожидаемое и актуальные значения поля email
	 */
	@Test
	public void getUserNegativeTest() {
		User user = new User(777, "Fedor", "Fedor", "Fedorov",
				"fedorov@post.com", "qwerty", "88005553555", 0);

		given()
				.spec(requestSpecification)
				.body(user)
				.when()
				.post("user")
				.then()
				.statusCode(200);// создаем юзера

		Response response = given()
				.spec(requestSpecification)
				.when()
				.get("user/janclodevandamme");

		System.out.println(response.asString());

		try {
			JSONAssert.assertEquals(new String(Files.readAllBytes(Paths.get("src/test/resources/badresponse.json"))),
					response.asString(), JSONCompareMode.NON_EXTENSIBLE);
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		//сравниваем ответ с json из файла
	}
}
