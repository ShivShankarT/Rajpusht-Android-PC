package in.rajpusht.pc.data.local.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.model.AwcSyncCount;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.model.BefRel;
import io.reactivex.Maybe;

@Dao
public abstract class AppDao {

    @Query(value ="select u.beneficiaryId, u.name,u.dob,u.stage,u.subStage,u.motherId,b.husbandName, b.name as motherName,p.pregnancyId,b.pctsId, CASE WHEN u.motherId IS NOT NULL                                                                                                             \n" +
            "       THEN case when julianday('now') - julianday(u.dob) <=91 then 'LM1'                                                                         \n" +
            "                 when julianday('now') - julianday(u.dob) <=182 then 'LM2'                                                                        \n" +
            "                     when julianday('now') - julianday(u.dob) <=365 then 'LM3'                                                                    \n" +
            "                     when julianday('now') - julianday(u.dob) <=547 then 'MY1'                                                                    \n" +
            "                         when julianday('now') - julianday(u.dob) <=730 then 'MY2'                                                                \n" +
            "                         when julianday('now') - julianday(u.dob) <=912 then 'MY3'                                                                \n" +
            "                         when julianday('now') - julianday(u.dob) <=1095 then 'MY4'                                                               \n" +
            "             when julianday('now') - julianday(u.dob) <=1155 then 'MY5'                                                                           \n" +
            "                        END                                                                                                                       \n" +
            "       ELSE case when julianday('now') - julianday(p.lmpDate) <=98 then 'PW1'                                                                     \n" +
            "                         when julianday('now') - julianday(p.lmpDate) <=196 then 'PW2'                                                            \n" +
            "                         when julianday('now') - julianday(p.lmpDate) <=252 then 'PW3'                                                            \n" +
            "                         when julianday('now') - julianday(p.lmpDate) <=280 then 'PW4'                                                            \n" +
            "        END                                                                                                                                       \n" +
            "       END AS currentSubStage, p.lmpDate,lm.id as lmFormId,pw.id as pwFormId,b.dataStatus                                                                  \n" +
            "                                                                                                                                                  \n" +
            "                                                                                                                                                  \n" +
            " from                                                                                                                                             \n" +
            "(select beneficiaryId , name,stage,subStage, dob,  NULL AS motherId from beneficiary                                                              \n" +
            "UNION                                                                                                                                             \n" +
            "select childId, NULL AS name,stage,subStage, dob, motherId AS motherId from child  ) u                                                            \n" +
            "Left Join pregnant p on p.beneficiaryId= u.beneficiaryId and u.motherId is NULL                                                                   \n" +
            "inner join  beneficiary b on (u.motherId is NULL  and b.beneficiaryId=u.beneficiaryId ) OR((u.motherId is NOT NULL  and b.beneficiaryId=u.motherId ))                                                                                                                                   \n" +
            "left join pw_monitor pw on pw.pregnancyId=p.pregnancyId and currentSubStage=pw.substage\n" +
            "left join lm_monitor lm on lm.childId=u.beneficiaryId and u.motherId is NOT NULL and currentSubStage=lm.substage\n" +
            "where b.awcCode=:awcCode and currentSubStage <> ''")
    public abstract LiveData<List<BefModel>> befModels(String awcCode);

    @Query("select * from beneficiary")
    public  abstract Maybe<List<BefRel>> befRels();

    @Query("select al.awcCode, al.awcEnglishName, u.dataStatus,ismother as isMother,  case when  ismother is not null then  count(*) else 0 end as count from assigned_location al\n" +
            "left join  (select beneficiaryId ,'Y' as ismother,  NULL AS motherId, awcCode,dataStatus from beneficiary                                                              \n" +
            "UNION                                                                                                                                             \n" +
            "select childId, 'N' as ismother, motherId AS motherId,awcCode,c.dataStatus from child\n" +
            " c inner join beneficiary m on m.beneficiaryId=c.motherId) u on  al.awcCode=u.awcCode\n" +
            " where dataStatus!=0\n" +
            "  group by  al.awcCode, ismother,u.dataStatus\n")
    public  abstract Maybe<List<AwcSyncCount>> awcViceSyncData();

}



