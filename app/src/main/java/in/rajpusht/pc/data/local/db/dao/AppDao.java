package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.model.BefModel;

@Dao
public abstract class AppDao {

    @Query(value ="select u.beneficiaryId, u.name,u.dob,u.stage,u.subStage,u.motherId,b.husbandName, b.name as motherName,p.pregnancyId,b.pctsId, CASE WHEN motherId IS NOT NULL                                                                                                             \n" +
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
            "       END AS currentSubStage, p.lmpDate,lm.id as lmFormId,pw.id as pwFormId                                                                  \n" +
            "                                                                                                                                                  \n" +
            "                                                                                                                                                  \n" +
            " from                                                                                                                                             \n" +
            "(select beneficiaryId , name,stage,subStage, dob,  NULL AS motherId from beneficiary                                                              \n" +
            "UNION                                                                                                                                             \n" +
            "select childId, NULL AS name,stage,subStage, dob, motherId AS motherId from child  ) u                                                            \n" +
            "Left Join pregnant p on p.beneficiaryId= u.beneficiaryId and u.motherId is NULL                                                                   \n" +
            "inner join  beneficiary b on (u.motherId is NULL  and b.beneficiaryId=u.beneficiaryId ) OR((u.motherId is NOT NULL  and b.beneficiaryId=u.motherId ))                                                                                                                                   \n" +
            "left join pw_monitor pw on pw.pregnancyId=p.pregnancyId and currentSubStage=pw.substage\n" +
            "left join lm_monitor lm on lm.childId=u.beneficiaryId and u.motherId is NOT NULL and currentSubStage=lm.substage")
    public abstract List<BefModel> befModels();
}
