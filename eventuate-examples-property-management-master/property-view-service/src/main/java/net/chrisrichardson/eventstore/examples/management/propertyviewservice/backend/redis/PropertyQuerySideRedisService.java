package net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.redis;

import net.chrisrichardson.eventstore.examples.management.property.common.Address;
import net.chrisrichardson.eventstore.examples.management.property.common.DeliveryTime;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import net.chrisrichardson.eventstore.examples.management.property.common.TimeRange;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.util.AvailablePropertyKeys;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.util.RedisEntityKeyFormatter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropertyQuerySideRedisService implements PropertyQuerySideService {
    private StringRedisTemplate redisTemplate;
    private RedisTemplate<String, PropertyInfo> propertyTemplate;

    private RedisEntityKeyFormatter keyFormatter = new RedisEntityKeyFormatter(PropertyInfo.class);

    public PropertyQuerySideRedisService(StringRedisTemplate redisTemplate, RedisTemplate<String, PropertyInfo> propertyTemplate) {
        this.redisTemplate = redisTemplate;
        this.propertyTemplate = propertyTemplate;
    }

    @Override
    public void add(final String id, final PropertyInfo property) {
        addPropertyInfo(id, property);
        addAvailabilityIndexEntries(id, property);
    }

    public void addPropertyInfo(String id, PropertyInfo property) {
        propertyTemplate.opsForValue().set(keyFormatter.key(id), property);
    }

    class ZSetEntry {

        public final String key;
        public final String value;
        public final int score;

        public ZSetEntry(String key, String value, int score) {
            this.key = key;
            this.value = value;
            this.score = score;
        }
    }

    private Stream<ZSetEntry> getEntries(String id, PropertyInfo property) {
        if(property==null || property.getOpeningHours()==null) {
            return Stream.empty();
        }
        return property.getOpeningHours().stream().flatMap ( tr -> {
            String indexValue = formatTrId(id, tr);
            int dayOfWeek = tr.getDayOfWeek();
            int closingTime = tr.getClosingTime();
            return property.getServiceArea().stream().map(zipCode ->
                    new ZSetEntry(closingTimesKey(zipCode, dayOfWeek), indexValue, closingTime)
            );
        });
    }

    private void addAvailabilityIndexEntries(String id, PropertyInfo property) {
        getEntries(id, property).forEach(entry -> redisTemplate.opsForZSet().add(entry.key, entry.value, entry.score));
    }

    private String closingTimesKey(String zipCode, int dayOfWeek) {
        return AvailablePropertyKeys.closingTimesKey(zipCode, dayOfWeek);
    }

    private String formatTrId(String id, TimeRange tr) {
        return tr.getOpeningTime() + "_" + id;
    }

    @Override
    public List<PropertyInfo> findAvailableProperty(Address deliveryAddress, DeliveryTime deliveryTime) {
        String zipCode = deliveryAddress.getZip();
        int dayOfWeek = deliveryTime.getDayOfWeek();
        int timeOfDay = deliveryTime.getTimeOfDay();
        String closingTimesKey = closingTimesKey(zipCode, dayOfWeek);

        Set<String> propertyIds =
                redisTemplate.opsForZSet().rangeByScore(closingTimesKey, timeOfDay, 2359).stream()
                .map(tr -> tr.split("_"))
                .filter(v -> Integer.parseInt(v[0]) <= timeOfDay)
                .map(v -> v[1])
                .collect(Collectors.toSet());

        Collection<String> keys = keyFormatter.keys(propertyIds);
        return propertyTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void delete(String id, PropertyInfo property) {
        getEntries(id, property).forEach(entry -> redisTemplate.opsForZSet().remove(entry.key, entry.value));
        redisTemplate.delete(keyFormatter.key(id));
    }

    @Override
    public PropertyInfo findById(String id) {
        return propertyTemplate.opsForValue().get(keyFormatter.key(id));
    }
}
