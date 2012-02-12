package net.mabako.zwickau.mensa.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import net.mabako.zwickau.mensa.R;

/**
 * Info-Fensterchen.
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class OptionInfo extends Option {
	/**
	 * Zeigt eine kleine Information an.
	 */
	@Override
	public boolean onSelected() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(R.string.info_title);
		builder.setMessage(R.string.info_str);
		
		builder.setPositiveButton(R.string.contact, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Email bei Klick auf 'Kontakt'
				final Intent email = new Intent(android.content.Intent.ACTION_SEND);
				email.setType("plain/text");
				email.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"mabako@gmail.com"});
				email.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mensa-App");
				activity.startActivity(Intent.createChooser(email, activity.getString(R.string.contact)));
			}
		});
		
		builder.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
		
		return false;
	}

	@Override
	public String getText() {
		return activity.getString(R.string.info);
	}

	@Override
	public boolean isVisible() {
		return true;
	}
}
