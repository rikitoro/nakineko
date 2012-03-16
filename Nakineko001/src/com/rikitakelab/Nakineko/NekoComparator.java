package com.rikitakelab.Nakineko;

import java.util.Comparator;

public class NekoComparator implements Comparator<Neko>{

	@Override
	public int compare(Neko object1, Neko object2) {
		// TODO Auto-generated method stub
		if (object1 == object2) {
			return 0;
		} else if (object1.getY() > object2.getY()) {
			return 1;
		} else {
			return -1;
		}
	}


}
