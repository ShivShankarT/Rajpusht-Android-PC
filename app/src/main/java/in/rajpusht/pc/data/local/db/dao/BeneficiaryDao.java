package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.model.BeneficiaryJoin;
import in.rajpusht.pc.model.DataStatus;
import io.reactivex.Single;

@Dao
public abstract class BeneficiaryDao extends BaseDao<BeneficiaryEntity> {

    @Query("select * from beneficiary")
    public abstract List<BeneficiaryEntity> getAllBeneficiaries();

    @Query("select * from beneficiary where beneficiaryId=:beneficiaryId")
    public abstract BeneficiaryEntity getBeneficiariesById(long beneficiaryId);

    @Query("select * from beneficiary where beneficiaryId=:beneficiaryId")
    public abstract Single<BeneficiaryJoin> getBeneficiaryDataById(long beneficiaryId);

    @Query("select * from beneficiary where dataStatus=:status")
    public abstract List<BeneficiaryEntity> getBeneficiaryNotSync(DataStatus status);

    @Query("Delete FROM beneficiary")
    public abstract void deleteAll();
}
