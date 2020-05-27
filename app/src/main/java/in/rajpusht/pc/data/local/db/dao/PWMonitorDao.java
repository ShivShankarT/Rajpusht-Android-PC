package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;


@Dao
public abstract class PWMonitorDao extends BaseDao<PWMonitorEntity> {
    @Query("select * from pw_monitor where pregnancyId=:pregnancyId")
    public abstract List<PWMonitorEntity> pwMonitor(long pregnancyId);
}
