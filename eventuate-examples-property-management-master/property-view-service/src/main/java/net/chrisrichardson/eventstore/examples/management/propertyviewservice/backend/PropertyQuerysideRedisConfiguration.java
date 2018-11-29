package net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend;

import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.redis.PropertyQuerySideRedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class PropertyQuerysideRedisConfiguration {

  @Bean
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(factory);
    return template;
  }

  @Bean
  public RedisTemplate<String, PropertyInfo> propertyTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, PropertyInfo> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setDefaultSerializer(new StringRedisSerializer());
    template.setValueSerializer(new JsonRedisPropertyInfoSerializer());
    return template;
  }

  @Bean
  public PropertyQuerySideRedisService propertyInfoUpdateService(RedisTemplate<String, PropertyInfo> propertyTemplate, StringRedisTemplate stringRedisTemplat) {
    return new PropertyQuerySideRedisService(stringRedisTemplat, propertyTemplate);
  }
}
