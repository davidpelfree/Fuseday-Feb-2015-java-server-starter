package tikal.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tikal.dos.AtomicCounter;
import tikal.model.Checkin;


@RestController
public class CheckinController {
    private AtomicCounter counter = new AtomicCounter();
	
	@RequestMapping(value="/checkin",method=RequestMethod.POST)
	public Object checkin(@RequestBody final Checkin checkin) {
		checkin.setTimestamp(System.currentTimeMillis());
		System.out.println(" - Num of Request: " + counter.increment());
		return true;
	}
}
