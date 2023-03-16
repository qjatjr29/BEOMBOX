package numble.mybox.config;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "numble.mybox")
@EnableMongoAuditing
@EnableAsync
@PropertySource("classpath:application.yml")
public class MongoDbConfig extends AbstractReactiveMongoConfiguration {

  @Value("${spring.data.mongodb.uri}")
  private String uri;

  @Override
  protected String getDatabaseName() {
    return "beombox";
  }

  @Override
  public MongoClient reactiveMongoClient() {
    return MongoClients.create(uri);
  }


  @Bean
  public ReactiveMongoTemplate reactiveMongoTemplate() {
    return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
  }

  @Override
  public ReactiveMongoDatabaseFactory reactiveMongoDbFactory() {
    return new SimpleReactiveMongoDatabaseFactory(reactiveMongoClient(), getDatabaseName());
  }

  @Bean
  public ReactiveMongoTransactionManager transactionManager(
      ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory) {
    return new ReactiveMongoTransactionManager(reactiveMongoDatabaseFactory);
  }

  @Bean
  public TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
    return TransactionalOperator.create(transactionManager);
  }

}
