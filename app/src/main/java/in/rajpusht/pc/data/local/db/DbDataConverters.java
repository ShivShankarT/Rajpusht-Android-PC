package in.rajpusht.pc.data.local.db;

import androidx.room.TypeConverter;

import java.util.Date;

import in.rajpusht.pc.model.DataStatus;


public class DbDataConverters {

    @TypeConverter
    public static Date toDate(long l) {
        return new Date(l);
    }

    @TypeConverter
    public static long fromDate(Date date) {
        return date.getTime();
    }


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


}
