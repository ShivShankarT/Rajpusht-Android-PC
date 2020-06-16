package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import io.reactivex.Maybe;

@Dao
public abstract class ChildDao extends BaseDao<ChildEntity> {

    @Query("select * from child where motherId=:motherId")
    public abstract List<ChildEntity> childEntities(long motherId);

    @Query("select * from child where childId=:childId")
    public abstract Maybe<ChildEntity> childEntity(long childId);

    @Query("Delete FROM child")
    public abstract void deleteAll();


}
