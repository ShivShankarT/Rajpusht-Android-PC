package in.rajpusht.pc.data.local.db;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.rajpusht.pc.model.DataStatus;


public class DbDataConverters {

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

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
    public Date toDate(String date) {
        if (date != null)
            try {
                return df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
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
