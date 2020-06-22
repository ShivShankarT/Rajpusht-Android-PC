package in.rajpusht.pc.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.data.local.db.AppDatabase;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.data.remote.AppApiHelper;
import in.rajpusht.pc.model.BeneficiaryWithRelation;
import in.rajpusht.pc.model.DataStatus;

@Deprecated
public class SyncUtils {
    private AppApiHelper appApiHelper;


    public void sync(AppDatabase appDatabase) {


        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();


        for (BeneficiaryEntity beneficiaryEntity : appDatabase.beneficiaryDao().getAllBeneficiaries()) {

            List<PregnantEntity> pregnantEntities = appDatabase.pregnantDao().getPregnantById(beneficiaryEntity.getBeneficiaryId());


            for (PregnantEntity pregnantEntity : pregnantEntities) {
                List<PWMonitorEntity> pwMonitorEntities = appDatabase.pwMonitorDao().pwMonitor(pregnantEntity.getPregnancyId());
                pregnantEntity.setPwMonitorEntities(pwMonitorEntities);
            }

            List<ChildEntity> childEntities = appDatabase.childDao().childEntities(beneficiaryEntity.getBeneficiaryId());

            for (ChildEntity childEntity : childEntities) {
                List<LMMonitorEntity> lmMonitorEntities = appDatabase.lmMonitorDao().lmMonitor(childEntity.getChildId());
                childEntity.setLmMonitorEntities(lmMonitorEntities);
            }

            JsonObject benefJson = (JsonObject) gson.toJsonTree(beneficiaryEntity);
            JsonArray childJsonArray = (JsonArray) gson.toJsonTree(childEntities);
            JsonArray pregnantJson = (JsonArray) gson.toJsonTree(pregnantEntities);
            benefJson.add("pregnant", pregnantJson);
            benefJson.add("child", childJsonArray);

            jsonArray.add(benefJson);
        }

        Log.i("sss", "sync: " + jsonArray.toString());

    }


    public JsonObject dataFor(BeneficiaryWithRelation beneficiaryWithRelation) {

        BeneficiaryEntity benfBeneficiaryEntity = beneficiaryWithRelation.beneficiaryEntity;
        JsonObject bjson = getBeneficiaryJson(benfBeneficiaryEntity);
        JsonArray pregJsonArray = new JsonArray();
        JsonArray childJsonArray = new JsonArray();
        for (PregnantEntity pregnantEntity : beneficiaryWithRelation.pregnantEntities) {
            JsonObject pregJson = getPregnantJson(pregnantEntity);
            JsonArray pwArray = new JsonArray();
            for (PWMonitorEntity pwMonitorEntity : beneficiaryWithRelation.pwMonitorEntities) {
                if (pwMonitorEntity.getPregnancyId() == pregnantEntity.getPregnancyId()) {
                    pwArray.add(getPwJson(pwMonitorEntity));
                }
            }
            pregJson.add("pwMonitorForms", pwArray);
            pregJsonArray.add(pregJson);
        }
        bjson.add("pregnant", pregJsonArray);


        for (ChildEntity childEntity : beneficiaryWithRelation.childEntities) {
            JsonObject childJson = getChildJson(childEntity);
            JsonArray lmArray = new JsonArray();
            for (LMMonitorEntity lmMonitorEntity : beneficiaryWithRelation.lmMonitorEntities) {
                if (lmMonitorEntity.getChildId() == childEntity.getChildId()) {
                    lmArray.add(getLmJson(lmMonitorEntity));
                }
            }
            childJson.add("pwMonitorForms", lmArray);
            childJsonArray.add(childJson);
        }
        bjson.add("child", childJsonArray);
        return bjson;

    }

    private JsonObject getBeneficiaryJson(BeneficiaryEntity beneficiaryEntity) {
        JsonObject beneficiaryobject = new JsonObject();
        if (beneficiaryEntity.getDataStatus() != DataStatus.NEW)
            beneficiaryobject.addProperty("beneficiaryId", beneficiaryEntity.getBeneficiaryId());
        beneficiaryobject.addProperty("awc_code", beneficiaryEntity.getAwcCode());
        beneficiaryobject.addProperty("name", beneficiaryEntity.getName());
        beneficiaryobject.addProperty("age", beneficiaryEntity.getAge());
        beneficiaryobject.addProperty("dob", AppDateTimeUtils.convertServerDate(beneficiaryEntity.getDob()));
        beneficiaryobject.addProperty("stage", beneficiaryEntity.getStage());
        beneficiaryobject.addProperty("sub_stage", beneficiaryEntity.getSubStage());
        beneficiaryobject.addProperty("no_of_child", beneficiaryEntity.getChildCount());
        beneficiaryobject.addProperty("pmmvy_installment", beneficiaryEntity.getPmmvyInstallment());
        beneficiaryobject.addProperty("igmpy_installment", beneficiaryEntity.getIgmpyInstallment());
        beneficiaryobject.addProperty("jsy_installment", beneficiaryEntity.getJsyInstallment());
        beneficiaryobject.addProperty("rajshri_installment", beneficiaryEntity.getRajshriInstallment());
        beneficiaryobject.addProperty("husband_father_name", beneficiaryEntity.getHusbandName());
        beneficiaryobject.addProperty("mobile_no", beneficiaryEntity.getMobileNo());
        beneficiaryobject.addProperty("husband_mobile_no", beneficiaryEntity.getHusbandMobNo());
        beneficiaryobject.addProperty("caste", beneficiaryEntity.getCaste());
        beneficiaryobject.addProperty("economic_status", beneficiaryEntity.getEconomic());
        beneficiaryobject.addProperty("pcts_id", beneficiaryEntity.getPctsId());
        beneficiaryobject.addProperty("bahamashah_or_ack_id", beneficiaryEntity.getBahamashahId());
        beneficiaryobject.addProperty("counseling_sms", beneficiaryEntity.getCounselingSms());
        beneficiaryobject.addProperty("created_by", beneficiaryEntity.getCreatedBy());
        beneficiaryobject.addProperty("createdAt", beneficiaryEntity.getCreatedAt());
        beneficiaryobject.addProperty("updatedAt", beneficiaryEntity.getUpdatedAt());


        beneficiaryobject.addProperty("dataStatus", beneficiaryEntity.getDataStatus() == DataStatus.NEW ? "NEW" : "EDIT");

        return beneficiaryobject;
    }

    private JsonObject getPregnantJson(PregnantEntity pregnantEntity) {
        JsonObject pregnantobject = new JsonObject();
        if (pregnantEntity.getDataStatus() != DataStatus.NEW)
            pregnantobject.addProperty("pregnancyId", pregnantEntity.getPregnancyId());
        pregnantobject.addProperty("beneficiary_id", pregnantEntity.getBeneficiaryId());
        pregnantobject.addProperty("lmp_date", AppDateTimeUtils.convertServerDate(pregnantEntity.getLmpDate()));
        pregnantobject.addProperty("createdAt", pregnantEntity.getCreatedAt());
        pregnantobject.addProperty("createdAt", pregnantEntity.getCreatedAt());
        pregnantobject.addProperty("updatedAt", pregnantEntity.getUpdatedAt());
        pregnantobject.addProperty("dataStatus", pregnantEntity.getDataStatus() == DataStatus.NEW ? "NEW" : "EDIT");
        return pregnantobject;
    }

    private JsonObject getChildJson(ChildEntity childEntity) {
        JsonObject childobject = new JsonObject();
        if (childEntity.getDataStatus() != DataStatus.NEW) {
            childobject.addProperty("childId", childEntity.getChildId());
            childobject.addProperty("mother_id", childEntity.getMotherId());
        }
        childobject.addProperty("dob", AppDateTimeUtils.convertServerDate(childEntity.getDob()));
        childobject.addProperty("age", childEntity.getAge());
        childobject.addProperty("stage", childEntity.getSubStage());
        childobject.addProperty("sub_stage", childEntity.getSubStage());
        childobject.addProperty("delivery_home", childEntity.getDeliveryHome());
        childobject.addProperty("delivery_place", childEntity.getDeliveryPlace());
        childobject.addProperty("child_order", childEntity.getChildOrder());
        childobject.addProperty("createdAt", childEntity.getCreatedAt());
        childobject.addProperty("updatedAt", childEntity.getUpdatedAt());
        childobject.addProperty("dataStatus", childEntity.getDataStatus() == DataStatus.NEW ? "NEW" : "EDIT");
        return childobject;
    }

    private JsonObject getLmJson(LMMonitorEntity lmMonitorEntity) {
        JsonObject lmObject = new JsonObject();
        if (lmMonitorEntity.getDataStatus() != DataStatus.NEW) {
            lmObject.addProperty("child_id", lmMonitorEntity.getChildId());
        }
        lmObject.addProperty("is_first_immunization_complete", HUtil.convYestoY(lmMonitorEntity.getIsFirstImmunizationComplete()));
        lmObject.addProperty("last_muac", lmMonitorEntity.getLastMuac());
        lmObject.addProperty("last_muac_check_date", AppDateTimeUtils.convertServerDate(lmMonitorEntity.getLastMuacCheckDate()));
        lmObject.addProperty("current_muac", lmMonitorEntity.getCurrentMuac());
        lmObject.addProperty("birth_weight", lmMonitorEntity.getBirthWeight());
        lmObject.addProperty("child_weight", lmMonitorEntity.getChildWeight());
        lmObject.addProperty("child_height", lmMonitorEntity.getChildHeight());
        lmObject.addProperty("pmmvy_installment", lmMonitorEntity.getPmmvyInstallment());
        lmObject.addProperty("igmpy_installment", lmMonitorEntity.getIgmpyInstallment());
        lmObject.addProperty("jsy_installment", lmMonitorEntity.getJsyInstallment());
        lmObject.addProperty("rajshri_installment", lmMonitorEntity.getRajshriInstallment());
        lmObject.addProperty("created_by", lmMonitorEntity.getCreatedBy());
        lmObject.addProperty("stage", lmMonitorEntity.getStage());
        lmObject.addProperty("sub_stage", lmMonitorEntity.getSubStage());
        lmObject.addProperty("createdAt", lmMonitorEntity.getCreatedAt());
        lmObject.addProperty("updatedAt", lmMonitorEntity.getUpdatedAt());


        return lmObject;
    }

    private JsonObject getPwJson(PWMonitorEntity pwMonitorEntity) {
        JsonObject pwobject = new JsonObject();
        if (pwMonitorEntity.getDataStatus() != DataStatus.NEW)
            pwobject.addProperty("pregnancy_id", pwMonitorEntity.getPregnancyId());
        pwobject.addProperty("stage", pwMonitorEntity.getStage());
        pwobject.addProperty("sub_stage", pwMonitorEntity.getSubStage());
        pwobject.addProperty("anc_count", pwMonitorEntity.getAncCount());
        pwobject.addProperty("last_anc", AppDateTimeUtils.convertServerDate(pwMonitorEntity.getLastAnc()));
        pwobject.addProperty("last_weight_in_mamta", pwMonitorEntity.getLastWeightInMamta());
        pwobject.addProperty("last_weight_check_date", AppDateTimeUtils.convertServerDate(pwMonitorEntity.getLastWeightCheckDate()));
        pwobject.addProperty("current_weight", pwMonitorEntity.getCurrentWeight());
        pwobject.addProperty("pmmvy_installment", pwMonitorEntity.getPmmvyInstallment());
        pwobject.addProperty("igmpy_installment", pwMonitorEntity.getIgmpyInstallment());
        pwobject.addProperty("jsy_installment", pwMonitorEntity.getJsyInstallment());
        pwobject.addProperty("rajshri_installment", pwMonitorEntity.getRajshriInstallment());
        pwobject.addProperty("created_by", pwMonitorEntity.getCreatedBy());
        pwobject.addProperty("createdAt", pwMonitorEntity.getCreatedAt());
        pwobject.addProperty("updatedAt", pwMonitorEntity.getUpdatedAt());

        return pwobject;
    }


}
