package tikal.controller;

import java.util.HashMap;
import java.util.Map;

import ch.hsr.geohash.GeoHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tikal.dos.AtomicCounter;
import tikal.model.Checkin;


@RestController
public class CheckinController {
    private AtomicCounter counter = new AtomicCounter();

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate<String,Long> longRedisTemplate;
	
	@RequestMapping(value="/checkin",method=RequestMethod.POST)
	public Object checkin(@RequestBody final Checkin checkin) {
		checkin.setTimestamp(System.currentTimeMillis());
		System.out.println(" - Num of Request: " + counter.increment());

        long now = System.currentTimeMillis();
        checkin.setTimestamp(now);
		System.out.println(checkin);

        final long nowAsSec = now / 1000;

        longRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                // Increment requests per second
                byte[] rpsKey = ("" + nowAsSec).getBytes();
                connection.hIncrBy("RPS".getBytes(), rpsKey, 1);
                connection.expire(rpsKey, 300);

                // Calc Geo Hash
                String area = GeoHash.withCharacterPrecision(checkin.getLatitude(), checkin.getLongitude(), 3).toBase32();
//                connection.incr(("area:" + area + ":userCount").getBytes());
                byte[] areaKey = "MM".getBytes();
                connection.hIncrBy(areaKey, area.getBytes(), 1);
                connection.expire(areaKey, 300);

                // User to area
                // TODO
                return null;
            }
        });




		return true;
	}

    @RequestMapping(value = "/keepAlive", method = RequestMethod.GET)
    public boolean keepAlive() {
        longRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.incr("keepAliveCount".getBytes());
                return null;
            }
        });
        return true;
    }
}
