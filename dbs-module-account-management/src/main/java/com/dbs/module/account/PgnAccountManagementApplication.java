package com.dbs.module.account;

import java.lang.reflect.Type;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import springfox.documentation.spring.web.json.Json;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan({ "com.dbs.module.account", 
                 "com.dbs.common.library", 
                 "com.dbs.common.database", 
                 "com.dbs.database.crm",
		 "com.dbs.common.base" })
@EntityScan(basePackages = { 
                             "com.dbs.database.crm.entities.product.*", 
                             "com.dbs.database.crm.entities.usermanagement",
                             "com.dbs.database.crm.entities.accountmanagement",
			     "com.dbs.database.crm.entities.product"})
//@EnableJpaRepositories("com.dbs.database.crm.repositories")
public class PgnAccountManagementApplication {

	@Bean
	public Gson gson() {
		return new GsonBuilder().serializeNulls().registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter())
				.registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
					@Override
					public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
						if (src == src.longValue())
							return new JsonPrimitive(src.longValue());
						return new JsonPrimitive(src);
					}
				}).create();

	}

	private static class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {
		@Override
		public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
			final JsonParser parser = new JsonParser();
			return parser.parse(json.value());
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(PgnAccountManagementApplication.class, args);
	}
}
