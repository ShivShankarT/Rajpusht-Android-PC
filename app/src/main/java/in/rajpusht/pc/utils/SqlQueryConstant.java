package in.rajpusht.pc.utils;

public class SqlQueryConstant {

    public static final String OTHER_WOMEN = "select u.beneficiaryId, u.name,u.dob,u.stage,u.subStage,u.motherId,b.husbandName, b.name as motherName,p.pregnancyId,b.pctsId, CASE WHEN u.motherId IS NOT NULL                                                                                                             \n" +
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
            "       END AS currentSubStage, p.lmpDate,lm.id as lmFormId,pw.id as pwFormId,b.dataStatus,      " +
            "                                                  \n" +
            "           CASE WHEN u.motherId IS NOT NULL  THEN  lm.naReason ELSE    pw.naReason  end  as  naReason,                                            \n" +
            "           CASE WHEN u.motherId IS NOT NULL  THEN  lm.available ELSE    pw.available  end  as  available                                          \n" +
            "                                                                                                                                                  \n" +
            "                                                                                                                                                  \n" +
            " from                                                                                                                                             \n" +
            "(select beneficiaryId , name,stage,subStage, dob,  NULL AS motherId from beneficiary where isActive='N'                                           \n" +
            "UNION                                                                                                                                             \n" +
            "select childId, NULL AS name,stage,subStage, dob, motherId AS motherId from child where isActive='N' ) u                                          \n" +
            "Left Join (select * from  pregnant) p on p.beneficiaryId= u.beneficiaryId and u.motherId is NULL                               \n" +
            "inner join  beneficiary b on (u.motherId is NULL  and b.beneficiaryId=u.beneficiaryId ) OR((u.motherId is NOT NULL  and b.beneficiaryId=u.motherId ))                                                                                                                                   \n" +
            "left join pw_monitor pw on pw.pregnancyId=p.pregnancyId and currentSubStage=pw.substage\n" +
            "left join lm_monitor lm on lm.childId=u.beneficiaryId and u.motherId is NOT NULL and currentSubStage=lm.substage\n" +
            "where b.awcCode=:awcCode  and currentSubStage <> '' and b.isActive <>'Y'";
}
