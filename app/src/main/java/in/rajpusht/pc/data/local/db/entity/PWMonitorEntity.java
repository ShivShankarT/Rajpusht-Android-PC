package in.rajpusht.pc.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = PWMonitorEntity.TABLE)
public class PWMonitorEntity {
    public static final String TABLE = "pw_monitor";

    @PrimaryKey
    private int id;

}
