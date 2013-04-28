package com.game_machine;

public final class ImmutableObject {

	public final int myint;
	public final String mystring;
	
	public ImmutableObject(int myint, String mystring) {
		this.myint = myint;
		this.mystring = mystring;
	}
	
	public ImmutableObject setMyint(int myint) {
		ImmutableObject object = new ImmutableObject(myint, this.mystring);
		return object;
	}
}
