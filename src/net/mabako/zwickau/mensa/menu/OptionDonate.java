package net.mabako.zwickau.mensa.menu;

import net.mabako.zwickau.mensa.MensaActivity;
import net.mabako.zwickau.mensa.R;
import net.robotmedia.billing.BillingController;

public class OptionDonate extends Option {

	@Override
	public boolean onSelected() {
		if(!activity.hasDonated())
			BillingController.requestPurchase(activity, MensaActivity.DONATE, true);
		return false;
	}

	@Override
	public String getText() {
		if(activity.hasDonated())
			return activity.getString(R.string.donate_thanks);
		return activity.getString(R.string.donate);
	}

	@Override
	public boolean isVisible(int count) {
		return true;
	}

	/**
	 * Kleiner Text für das Dropdown-Menü für ICS.
	 * 
	 * @return
	 */
	public String getDropdownText() {
		if(!activity.hasDonated())
			return "1€";
		return super.getDropdownText();
	}
}
