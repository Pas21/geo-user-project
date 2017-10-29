package server.backend.wrapper.test;

import commons.InvalidUsernameException;
import commons.Posizione;
import commons.User;
import server.backend.UserRegistry;

public class Test1 {
	public static void main(String[] args) throws InvalidUsernameException{
		User u=new User("varTheBest","lalala","gigino@gmail.com","Antonio","Varone");
		Posizione p=new Posizione("20","02/16/2017 09:56:47","41.090774", "14.498640");
		u.addPosition(p);
		UserRegistry ur= new UserRegistry();
		ur.add(u);
	}
}
