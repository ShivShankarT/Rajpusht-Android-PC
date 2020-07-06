package in.rajpusht.pc.data.local.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import in.rajpusht.pc.model.AwcSyncCount;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.model.BeneficiaryWithRelation;
import in.rajpusht.pc.utils.SqlQueryConstant;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public abstract class AppDao {

    @Query(value ="select u.beneficiaryId, u.name,u.dob,u.stage,u.subStage,u.motherId,b.husbandName, b.name as motherName,p.pregnancyId,b.pctsId, CASE WHEN u.motherId IS NOT NULL                                                                                                             \n" +
            "       THEN case when julianday('now') - julianday(u.dob) <=91 then 'LM1'                                                                         \n" +
            "                 when julianday('now') - julianday(u.dob) <=182 then 'LM2'                                                                        \n" +
            "                     when julianday('now') - julianday(u.dob) <=365 then 'MY1'                                                                    \n" +
            "                         when julianday('now') - julianday(u.dob) <=547 then 'MY2'                                                                \n" +
            "                         when julianday('now') - julianday(u.dob) <=730 then 'MY3'                                                                \n" +
            "                        END                                                                                                                       \n" +
            "       ELSE case when julianday('now') - julianday(p.lmpDate) <=98 then 'PW1'                                                                     \n" +
            "                         when julianday('now') - julianday(p.lmpDate) <=196 then 'PW2'                                                            \n" +
            "                         when julianday('now') - julianday(p.lmpDate) <=252 then 'PW3'                                                            \n" +
            "                         when julianday('now') - julianday(p.lmpDate) <=280 then 'PW4'                                                            \n" +
            "        END                                                                                                                                       \n" +
            "       END AS currentSubStage, p.lmpDate,lm.id as lmFormId,pw.id as pwFormId,u.dataStatus,u.childOrder,      " +
            "                                                  \n" +
            "           CASE WHEN u.motherId IS NOT NULL  THEN  lm.naReason ELSE    pw.naReason  end  as  naReason,                                            \n" +
            "           CASE WHEN u.motherId IS NOT NULL  THEN  lm.available ELSE    pw.available  end  as  available                                          \n" +
            "                                                                                                                                                  \n" +
            "                                                                                                                                                  \n" +
            " from                                                                                                                                             \n" +
            "(select beneficiaryId , name,stage,subStage, dob,  NULL AS motherId,NULL as childOrder,dataStatus from beneficiary where isActive='Y'                        \n" +
            "UNION                                                                                                                                             \n" +
            "select childId, NULL AS name,stage,subStage, dob, motherId AS motherId, childOrder,dataStatus from child where isActive='Y' ) u                              \n" +
            "Left Join (select * from  pregnant where isActive='Y') p on p.beneficiaryId= u.beneficiaryId and u.motherId is NULL                               \n" +
            "inner join  beneficiary b on (u.motherId is NULL  and b.beneficiaryId=u.beneficiaryId ) OR((u.motherId is NOT NULL  and b.beneficiaryId=u.motherId ))                                                                                                                                   \n" +
            "left join pw_monitor pw on pw.pregnancyId=p.pregnancyId and currentSubStage=pw.substage\n" +
            "left join lm_monitor lm on lm.childId=u.beneficiaryId and u.motherId is NOT NULL and currentSubStage=lm.substage\n" +
            "where b.awcCode=:awcCode  and currentSubStage <> '' and b.isActive='Y'  order by u.motherId, u.beneficiaryId")
    public abstract LiveData<List<BefModel>> befModels(String awcCode);//b.awcCode=:awcCode


    @Query(SqlQueryConstant.OTHER_WOMEN)
    public abstract LiveData<List<BefModel>> otherWomenBefModels(String awcCode);//b.awcCode=:awcCode

    @Query("select * from beneficiary where beneficiaryId  in ( \n" +
            "   select b.beneficiaryId from beneficiary b \n" +
            "   left join pregnant p on p.beneficiaryId =b.beneficiaryId\n" +
            "   left join child c on c.motherId =b.beneficiaryId\n" +
            "   where b.dataStatus!=0 or c.dataStatus!=0 or p.dataStatus!=0 \n" +
            "  )")
    @Transaction
    public  abstract Single<List<BeneficiaryWithRelation>> getAllNotSyncBeneficiaryWithRelation();

    @Query("select al.awcCode, al.awcEnglishName, u.dataStatus,ismother as isMother,  case when  ismother is not null then  count(*) else 0 end as count from assigned_location al\n" +
            "left join  (select beneficiaryId ,'Y' as ismother,  NULL AS motherId, awcCode,dataStatus from beneficiary                                                              \n" +
            "UNION                                                                                                                                             \n" +
            "select childId, 'N' as ismother, motherId AS motherId,awcCode,c.dataStatus from child\n" +
            " c inner join beneficiary m on m.beneficiaryId=c.motherId) u on  al.awcCode=u.awcCode\n" +
            " where dataStatus!=0\n" +
            "  group by  al.awcCode, ismother,u.dataStatus\n")
    public  abstract Observable<List<AwcSyncCount>> awcViceSyncData();

}



