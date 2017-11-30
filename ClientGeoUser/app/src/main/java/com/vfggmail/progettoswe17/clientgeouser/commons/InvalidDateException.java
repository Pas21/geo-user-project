package com.vfggmail.progettoswe17.clientgeouser.commons;


//Eccezione generata quando le date inserite non sono valide
public class InvalidDateException extends Exception {

	private static final long serialVersionUID = -2161808073357292179L;
	public InvalidDateException(String msg){
		super(msg);
	}
}
