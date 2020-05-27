package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;

@Dao
public abstract class BeneficiaryDao extends BaseDao<BeneficiaryEntity> {

    @Query("select * from beneficiary")
    public abstract List<BeneficiaryEntity> getAllBeneficiaries();

}
