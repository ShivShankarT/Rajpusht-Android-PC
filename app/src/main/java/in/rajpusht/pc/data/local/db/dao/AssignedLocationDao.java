package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.AssignedLocationEntity;
import in.rajpusht.pc.model.AwcStageCount;
import io.reactivex.Maybe;

@Dao
public abstract class AssignedLocationDao extends BaseDao<AssignedLocationEntity> {

    @Query("Delete FROM " + AssignedLocationEntity.TABLE_NAME)
    public abstract void deleteAll();

    @Query("SELECT * FROM " + AssignedLocationEntity.TABLE_NAME)
    public abstract Maybe<List<AssignedLocationEntity>> getAllAssignedLocation();


    @Query("select t.awcCode,substr(t.currentSubStage,0,3) as stage ,sum(c) as count  from(\n" +
            "select b.awcCode, CASE WHEN u.motherId IS NOT NULL                                                                                                             \n" +
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
            "       END AS currentSubStage ,count(*) as c                                                                                                         \n" +
            "                                                                                                                                                                                                                                                                                                  \n" +
            " from                                                                                                                                             \n" +
            "(select beneficiaryId , name,stage,subStage, dob,  NULL AS motherId from beneficiary                                                              \n" +
            "UNION                                                                                                                                             \n" +
            "select childId, NULL AS name,stage,subStage, dob, motherId AS motherId from child  ) u                                                            \n" +
            "Left Join pregnant p on p.beneficiaryId= u.beneficiaryId and u.motherId is NULL                                                                   \n" +
            "inner join  beneficiary b on (u.motherId is NULL  and b.beneficiaryId=u.beneficiaryId ) OR((u.motherId is NOT NULL  and b.beneficiaryId=u.motherId ))                                                                                                                                   \n" +
            "left join pw_monitor pw on pw.pregnancyId=p.pregnancyId and currentSubStage=pw.substage\n" +
            "left join lm_monitor lm on lm.childId=u.beneficiaryId and u.motherId is NOT NULL and currentSubStage=lm.substage\n" +
            "where currentSubStage <> '' and lm.id is  null and pw.id is  null\n" +
            "group By b.awcCode,currentSubStage\n" +
            ") t\n" +
            "group By t.awcCode,stage")
    public abstract Maybe<List<AwcStageCount>> awcStageCount();


}
