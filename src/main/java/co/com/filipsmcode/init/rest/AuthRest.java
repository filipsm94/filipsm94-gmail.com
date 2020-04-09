package co.com.filipsmcode.init.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.filipsmcode.init.dao.UserDao;
import co.com.filipsmcode.init.entity.User;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/auth")
public class AuthRest {
	
	@Autowired
	private UserDao userDao;
	
	
	@PostMapping()
	public ResponseEntity<Boolean> getAccessForUsername(@RequestBody User user){
		List<User> response = this.userDao.findUserByUsername(user.getUsername());
		if(!response.isEmpty()) {
			User userExist = response.get(0);
			if(userExist.getPass().equals(user.getPass())) {
				return ResponseEntity.ok(Boolean.TRUE);			
			}else {
				return ResponseEntity.ok(Boolean.FALSE);	
			}
		}else {
			return ResponseEntity.ok(Boolean.FALSE);
		}
		
	}

}
