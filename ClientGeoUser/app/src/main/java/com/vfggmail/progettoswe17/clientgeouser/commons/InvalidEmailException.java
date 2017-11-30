package com.vfggmail.progettoswe17.clientgeouser.commons;



public class InvalidEmailException extends Exception{
    private static final long serialVersionUID = -2161808073357292179L;
    public InvalidEmailException(String msg){
        super(msg);
    }
}
