package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.model.DataStatus;


@Dao
public abstract class PregnantDao extends BaseDao<PregnantEntity> {
    @Query("select * from pregnant where beneficiaryId=:beneficiaryId")
    public abstract List<PregnantEntity> getPregnantById(long beneficiaryId);

    @Query("select * from pregnant where pregnancyId=:pregnancyId")
    public abstract PregnantEntity getPregnantByPregnancyId(long pregnancyId);

    @Query("Delete FROM pregnant")
    public abstract void deleteAll();

}
