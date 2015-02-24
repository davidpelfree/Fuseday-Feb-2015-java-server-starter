package tikal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

        long nowAsSec = now % 1000;

        long rpsKey = nowAsSec;
        longRedisTemplate.<Long,Long>opsForHash().increment("meta", rpsKey, 1L);


//        // Using set to set value
//        String key = "user:" + checkin.getUserId();
//        String value = checkin.getLatitude() + ":" + checkin.getLongitude() + ":" + checkin.getTimestamp();
//        stringRedisTemplate.opsForValue().set(key, value);
//        stringRedisTemplate.opsForValue().set("S", "Shyam");
//
//        //Fetch values from set
//        System.out.println(stringRedisTemplate.opsForValue().get("R"));
//        System.out.println(stringRedisTemplate.opsForValue().get("S"));

		return true;
	}

    @RequestMapping(value = "/keepAlive", method = RequestMethod.GET)
    public boolean keepAlive() {
        return true;
    }
}
