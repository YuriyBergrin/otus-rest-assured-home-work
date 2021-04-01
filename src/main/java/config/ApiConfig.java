package config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;


public class ApiConfig {
	protected PropertiesConfig cfg = ConfigFactory.create(PropertiesConfig.class);

	protected RequestSpecification requestSpecification = new RequestSpecBuilder()
			.setBaseUri(cfg.baseurl())
			.setBasePath(cfg.basepath())
			.setContentType(ContentType.JSON)
			.build();

	protected ResponseSpecification responseSpecification = new ResponseSpecBuilder()
			.expectStatusCode(405)
			.build();
}
