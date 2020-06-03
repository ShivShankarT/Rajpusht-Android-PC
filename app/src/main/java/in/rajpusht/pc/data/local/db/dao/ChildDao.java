package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;

@Dao
public abstract class ChildDao extends BaseDao<ChildEntity> {

    @Query("select * from child where motherId=:motherId")
    public abstract List<ChildEntity> childEntities(long motherId);


    @Query("select * from child where isNew is not null")
    public  abstract List<ChildEntity> getChildDataNotSync();

}
