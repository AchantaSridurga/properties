package net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class JsonRedisPropertyInfoSerializer implements RedisSerializer<PropertyInfo> {

    private final ObjectMapper om;

    public JsonRedisPropertyInfoSerializer() {
        this.om = new ObjectMapper();
    }

    @Override
    public byte[] serialize(PropertyInfo t) throws SerializationException {
        try {
            return om.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    @Override
    public PropertyInfo deserialize(byte[] bytes) throws SerializationException {

        if (bytes == null) {
            return null;
        }

        try {
            return om.readValue(bytes, PropertyInfo.class);
        } catch (Exception e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }
}