package in.rajpusht.pc.data.local.db;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.rajpusht.pc.model.DataStatus;


public class DbDataConverters {

    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private Gson gson = new Gson();
    private static final ThreadLocal<SimpleDateFormat> dateformatter1 =
            new ThreadLocal<SimpleDateFormat>() {
                @Override protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("ddMMM");
                }
            };
    @TypeConverter
    public static DataStatus dataStatus(Integer dataStatus) {
        if (dataStatus == null)
            return null;
        return DataStatus.values()[dataStatus];
    }

    @TypeConverter
    public static Integer dataStatus(DataStatus dataStatus) {
        if (dataStatus == null)
            return null;
        return dataStatus.value;
    }

    @TypeConverter
    public List<String> toListString(String data) {
        if (data != null)
            try {
                return gson.fromJson(data, new TypeToken<List<String>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        return Collections.emptyList();
    }

    @TypeConverter
    public String fromListString(List<String> data) {
        if (data != null)
            return gson.toJson(data);
        return null;
    }

    @TypeConverter
    public synchronized Date toDate(String date) {
        if (!TextUtils.isEmpty(date))
            synchronized(df) {
                try {
                    return df.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        return null;
    }

    @TypeConverter
    public String fromDate(Date date) {
        if (date != null)
            return df.format(date);
        return null;
    }


}
