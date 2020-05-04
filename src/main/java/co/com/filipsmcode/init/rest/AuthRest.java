package co.com.filipsmcode.init.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.filipsmcode.init.dao.UserDao;
import co.com.filipsmcode.init.entity.User;
import co.com.filipsmcode.init.models.AuthenticationRequest;
import co.com.filipsmcode.init.models.AuthenticationResponse;
import co.com.filipsmcode.init.sevices.MyUserDetailsService;
import co.com.filipsmcode.init.utils.JwtUtil;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/auth")
public class AuthRest {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService  myUserDetailsService;
	
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@PostMapping
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
	
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public ResponseEntity<?> loginAccess(@RequestBody AuthenticationRequest auth) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUserName(), auth.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("incorrect password");
		}
		
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(auth.getUserName());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@RequestBody User user) throws Exception {
		return ResponseEntity.ok(myUserDetailsService.updateUser(user));
	}
	
	
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody User user) throws Exception {
		return ResponseEntity.ok(myUserDetailsService.save(user));
	}

}
