package net.mabako.zwickau.mensa;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final String TABLE = "essen";

	public DatabaseHandler(Context context) {
		super(context, "zwickau_mensa", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE + " (name text, preis text, date int, mensa text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public void deleteAllFood()
	{
		getWritableDatabase().delete(TABLE, "1", new String[]{});
	}
	
	public void deleteOldFood()
	{
		Calendar c = Calendar.getInstance();
		
		String params = String.format("%04d%02d%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
		getWritableDatabase().delete(TABLE, "date < ?", new String[]{params});
	}

	public boolean loadPlan(Mensa mensa, boolean naechsteWoche) {
		SQLiteDatabase db = getWritableDatabase();
		MensaPlan plan = mensa.getPlan();
		Calendar c = getCalendar(naechsteWoche);
		
		boolean foundAny = false;
		for(int i = 2 + (naechsteWoche ? 7 : 0); i < (naechsteWoche ? 14 : 7 ); ++ i)
		{
			c.add(Calendar.DAY_OF_MONTH, 1);
			String param = String.format("%04d%02d%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
			
			Cursor cursor = db.query(TABLE, new String[] { "name", "preis" }, "date = ? AND mensa = ?", new String[]{param, mensa.toString()}, null, null, null, null);
			
			LinkedList<Essen> essen = new LinkedList<Essen>();
			while(cursor.moveToNext())
			{
				foundAny = true;
				essen.add(new Essen(cursor.getString(0), cursor.getString(1)));
			}
			plan.put(i, essen);
			cursor.close();
		}
		return foundAny;
	}
	
	public void savePlan(Mensa mensa, boolean naechsteWoche) {
		SQLiteDatabase db = getWritableDatabase();
		MensaPlan plan = mensa.getPlan();
		Calendar c = getCalendar(naechsteWoche);
		
		for(int i = 2 + (naechsteWoche ? 7 : 0); i < (naechsteWoche ? 14 : 7); ++ i)
		{
			c.add(Calendar.DAY_OF_MONTH, 1);
			String param = String.format("%04d%02d%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
			db.delete(TABLE, "date = ? AND mensa = ?", new String[]{param, mensa.toString()});
			
			List<Essen> essen = plan.get(i);
			if(essen != null && essen.size() > 0)
			{
				for(Essen e : essen)
				{
					ContentValues cv = new ContentValues();
					cv.put("name", e.get(Essen.TEXT));
					cv.put("preis", e.get(Essen.TEXT2));
					cv.put("date", param);
					cv.put("mensa", mensa.toString());
					db.insert(TABLE, null, cv);
				}
			}
		}
	}
	
	public Calendar getCalendar(boolean naechsteWoche) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -MensaActivity.getInstance().getToday() + 1 + (naechsteWoche ? 7 : 0));
		return c;
	}
}
