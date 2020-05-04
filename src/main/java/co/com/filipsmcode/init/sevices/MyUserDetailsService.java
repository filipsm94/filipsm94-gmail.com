package co.com.filipsmcode.init.sevices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.query.criteria.internal.expression.function.CurrentTimeFunction;
import org.hibernate.query.criteria.internal.expression.function.CurrentTimestampFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.com.filipsmcode.init.dao.UserDao;
import co.com.filipsmcode.init.entity.User;

@Service
public class MyUserDetailsService implements UserDetailsService{

	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		List<User> users = userDao.findUserByUsername(userName);
		if(users.isEmpty()) {
			throw new UsernameNotFoundException("User not found with username: " + userName);
		}
		User user = users.get(0);
		return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPass(), new ArrayList<>()); 
	}
	
	public User save(User user) {
		User newUser = new User();
		newUser.setFechaCreacion(new Date().toGMTString());
		newUser.setUsername(user.getUsername());
		newUser.setPass(bcryptEncoder.encode(user.getPass()));
		newUser.setFullName(user.getFullName());
		if(user.getId()!= null) {			
			newUser.setId(user.getId());
		}
		return userDao.save(newUser);
	}
	
	public User updateUser(User user) {
		List<User> users = userDao.findUserByUsername(user.getUsername());
		if(users.isEmpty()) {
			throw new UsernameNotFoundException("User not found with username: " + user.getUsername());
		}
		User userUpdated = users.get(0);
		userUpdated.setPass(user.getPass());
		return save(userUpdated);
	}

}
