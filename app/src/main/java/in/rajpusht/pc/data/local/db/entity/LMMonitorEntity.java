package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = LMMonitorEntity.TABLE)
public class LMMonitorEntity {
    public static final String TABLE = "lm_monitor";

    @PrimaryKey
    private int id;

}
