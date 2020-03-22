package com.company.org.dao.mongo;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDatabase {

    @Autowired
    Environment env;

    @Bean
    public MongoOperations mongoOperations() {

        String host = env.getProperty("spring.data.mongodb.host");
        int port = Integer.parseInt(env.getProperty("spring.data.mongodb.port"));
        return new MongoTemplate(new MongoClient(host, port), env.getProperty("spring.data.mongodb.database"));
    }
}
