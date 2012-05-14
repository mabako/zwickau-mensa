package net.mabako.zwickau.mensa;

import java.util.Vector;

import com.viewpagerindicator.TitleProvider;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ListAdapter;

/**
 * PagerAdapter
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 */
public class MensaPagerAdapter extends PagerAdapter implements OnPageChangeListener, TitleProvider {
	private Vector<TitledListView> views = new Vector<TitledListView>();
		
	@Override
	public int getCount() {
		return views.size();
	}

	/**
	 * Create the page for the given position. The adapter is responsible
	 * for adding the view to the container given here, although it only
	 * must ensure this is done by the time it returns from
	 * {@link #finishUpdate()}.
	 * 
	 * @param container
	 *            The containing View in which the page will be shown.
	 * @param position
	 *            The page position to be instantiated.
	 * @return Returns an Object representing the new page. This does not
	 *         need to be a View, but can be some other container of the
	 *         page.
	 */
	@Override
	public Object instantiateItem(View collection, int position) {
		TitledListView view = views.get(position);
		((ViewPager) collection).addView(view);
		return view;
	}

	/**
	 * Remove a page for the given position. The adapter is responsible for
	 * removing the view from its container, although it only must ensure
	 * this is done by the time it returns from {@link #finishUpdate()}.
	 * 
	 * @param container
	 *            The containing View from which the page will be removed.
	 * @param position
	 *            The page position to be removed.
	 * @param object
	 *            The same object that was returned by
	 *            {@link #instantiateItem(View, int)}.
	 */
	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((TitledListView) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	/**
	 * Called when the a change in the shown pages has been completed. At
	 * this point you must ensure that all of the pages have actually been
	 * added or removed from the container as appropriate.
	 * 
	 * @param container
	 *            The containing View which is displaying this adapter's
	 *            page views.
	 */
	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

	public void onPageScrollStateChanged(int state) {
	}

	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	public void onPageSelected(int position) {
	}

	/**
	 * Fügt eine neue Seite hinzu.
	 * 
	 * @param day
	 * @param context
	 * @param adapter
	 */
	public void addPage(int day, Context context, ListAdapter adapter) {
		TitledListView view = new TitledListView(day, context);
		view.setAdapter(adapter);
		views.add(view);
	}

	/**
	 * Gibt den Titel einer beliebigen Seite zurück.
	 */
	public String getTitle(int position) {
		return MensaActivity.getInstance().getNameOfDay(views.get(position).getDay());
	}

	/**
	 * Löscht alle Seiten.
	 */
	public void clear() {
		//TODO testen -> Mensawechsel
		views.clear();
	}
}
