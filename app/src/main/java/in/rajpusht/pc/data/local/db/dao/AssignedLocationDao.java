package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.AssignedLocationEntity;
import io.reactivex.Maybe;

@Dao
public abstract class AssignedLocationDao extends BaseDao<AssignedLocationEntity> {

    @Query("Delete FROM " + AssignedLocationEntity.TABLE_NAME)
    public abstract void deleteAll();

    @Query("SELECT * FROM " + AssignedLocationEntity.TABLE_NAME)
    public abstract Maybe<List<AssignedLocationEntity>> getAllAssignedLocation();


}
