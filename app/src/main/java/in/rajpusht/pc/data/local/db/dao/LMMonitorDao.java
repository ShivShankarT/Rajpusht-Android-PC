package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import io.reactivex.Maybe;
import io.reactivex.Single;


@Dao
public abstract class LMMonitorDao extends BaseDao<LMMonitorEntity> {

    @Query("select * from lm_monitor where childId=:childId")
    public abstract List<LMMonitorEntity> lmMonitor(long childId);


    @Query("select * from lm_monitor where id=:id")
    public abstract Maybe<LMMonitorEntity> lmMonitorById(long id);



}
