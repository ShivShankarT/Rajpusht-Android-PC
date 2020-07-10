package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.LocationEntity;
import io.reactivex.Single;

@Dao
public abstract class LocationDao extends BaseDao<LocationEntity> {

    @Query("select * from beneficiary_location")
    public abstract Single<List<LocationEntity>> getLocationEntities();

}
