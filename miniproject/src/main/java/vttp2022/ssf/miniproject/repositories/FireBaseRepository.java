package vttp2022.ssf.miniproject.repositories;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class FireBaseRepository {
    
    @Autowired @Qualifier("redislab")
	private RedisTemplate<String, String> redisTemplate;

    public void saveKey(String email, String payload) {
        ValueOperations<String, String> valueOp= redisTemplate.opsForValue();
        valueOp.set(email, payload, 100, TimeUnit.MINUTES);
        System.out.printf("%s is saved\n", email);
    }

    public Set<String> getAllKeys() {
        Set<String> redisKeys = redisTemplate.keys("*@*");
        System.out.println(redisTemplate.keys("*@*"));

        // Returns a Container describing the given non-null value.
        // box with data
        return redisKeys;
    }

    public String getValue(String key) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        // We retrieve the city information from the address
        String value = valueOp.get(key);

        return value;
    }

    // public void update(String emailId, String newPayload) {
    //     ValueOperations<String, String> valueOp= redisTemplate.opsForValue();
    //     valueOp.set(emailId, newPayload);
    //     System.out.println("Favourite weather successfully updated!");
    // }

    // public boolean emailExists(String email) {
    //     return redisTemplate.hasKey(email);
    // }
}
