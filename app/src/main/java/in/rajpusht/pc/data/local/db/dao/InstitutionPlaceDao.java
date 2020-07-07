package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.custom.ui.DropDownModel;
import in.rajpusht.pc.data.local.db.entity.InstitutionPlaceEntity;
import io.reactivex.Single;

@Dao
public abstract class InstitutionPlaceDao extends BaseDao<InstitutionPlaceEntity> {


    //    private int id;
    //    private String district;
    //    private String block;
    //    private String typeOfFacility;
    //    private String location;

    @Query("select district from " + InstitutionPlaceEntity.TABLE_NAME)
    abstract Single<List<String>> getAllDistrict();

    @Query("select count(*) from " + InstitutionPlaceEntity.TABLE_NAME)
    public abstract Single<Long> getInstitutionPlaceCount();

    @Query("select block from " + InstitutionPlaceEntity.TABLE_NAME)
    abstract Single<List<String>> getAllBlock();

    @Query("select id, location || '(' || block || ')' as title from " + InstitutionPlaceEntity.TABLE_NAME + " where typeOfFacility =:type order by location asc")
    public abstract Single<List<DropDownModel>> getInstitutionLocation(String type);

    @Query("Delete FROM institution_place")
    public abstract void deleteAll();
}

