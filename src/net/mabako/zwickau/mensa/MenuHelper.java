package net.mabako.zwickau.mensa;

import android.os.Build;

public class MenuHelper {
	public static MenuHelper getInstance() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return new MenuICS();
		} else {
			return new MenuHelper();
		}
	}

	public void updateTitle(int day) {
	}
}
