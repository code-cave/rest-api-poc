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
        /* Gotta connect to mongo somehow
         * Making what is a template object a bean will make it
         * so all operations are able to be done independently
         * Of course, if the required properties are not provided
         * the application will not connect to the Mongo database
         */
        String host = env.getProperty("spring.data.mongodb.host");
        int port = Integer.parseInt(env.getProperty("spring.data.mongodb.port"));
        return new MongoTemplate(new MongoClient(host, port), env.getProperty("spring.data.mongodb.database"));
    }
}
