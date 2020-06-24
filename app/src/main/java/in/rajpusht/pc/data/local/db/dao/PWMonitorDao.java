package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import io.reactivex.Maybe;
import io.reactivex.Single;


@Dao
public abstract class PWMonitorDao extends BaseDao<PWMonitorEntity> {

    @Query("select * from pw_monitor where pregnancyId=:pregnancyId")
    public abstract List<PWMonitorEntity> pwMonitor_(long pregnancyId);


    @Query("select * from pw_monitor where pregnancyId=:pregnancyId")
    public abstract Single<List<PWMonitorEntity>> pwMonitor(long pregnancyId);

    @Query("select * from pw_monitor where id=:id")
    public abstract Maybe<PWMonitorEntity> pwMonitorByID(long id);


    @Query("Delete FROM pw_monitor")
    public abstract void deleteAll();


}
