package net.mabako.zwickau.mensa;

import android.os.Build;

/**
 * Simpler Menühelfer für verschiedene Androidversionen.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class MenuHelper {
	public static MenuHelper getInstance() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new MenuActionBar();
		} else {
			return new MenuHelper();
		}
	}

	public void updateTitle(int day) {
	}
}
