package in.rajpusht.pc.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import in.rajpusht.pc.custom.utils.HUtil;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.model.BefRel;
import in.rajpusht.pc.model.DataStatus;
import in.rajpusht.pc.model.Quintet;

import static in.rajpusht.pc.utils.JsonUtils.getDouble;
import static in.rajpusht.pc.utils.JsonUtils.getInt;
import static in.rajpusht.pc.utils.JsonUtils.getLong;
import static in.rajpusht.pc.utils.JsonUtils.getString;

public class JsonParser {


    public static Quintet<List<BeneficiaryEntity>, List<PregnantEntity>,
            List<ChildEntity>,
            List<PWMonitorEntity>, List<LMMonitorEntity>> parseBenfData(ApiResponse<JsonObject> apiResponse) {

        if (apiResponse != null && apiResponse.isStatus()) {
            JsonObject jsonObject = apiResponse.getData();
            try {
                JsonArray beneficiarylist = jsonObject.getAsJsonArray("beneficiary");
                JsonArray pregnantlist = jsonObject.getAsJsonArray("pregnant");
                JsonArray pw_monitorlist = jsonObject.getAsJsonArray("pw_monitor");
                JsonArray childlist = jsonObject.getAsJsonArray("child");
                JsonArray lm_monitorlist = jsonObject.getAsJsonArray("lm_monitor");

                List<BeneficiaryEntity> beneficiaryModels = new ArrayList<>();
                List<PregnantEntity> pregnantModels = new ArrayList<>();
                List<PWMonitorEntity> pwMonitorModels = new ArrayList<>();
                List<ChildEntity> childModel = new ArrayList<>();
                List<LMMonitorEntity> lmMonitorModel = new ArrayList<>();


                for (int i = 0; i < beneficiarylist.size(); i++) {

                    JsonObject object = beneficiarylist.get(i).getAsJsonObject();
                    BeneficiaryEntity bmodel = new BeneficiaryEntity();
                    int id = getInt(object, "id");
                    String awc_code = getString(object, "awc_code");
                    String name = getString(object, "name");
                    Integer age = getInt(object, "age");
                    String dob = getString(object, "dob");
                    String stage = getString(object, "stage");
                    String sub_stage = getString(object, "sub_stage");
                    Integer no_of_child = getInt(object, "no_of_child");
                    Integer pmmvy_installment = getInt(object, "pmmvy_installment");
                    Integer igmpy_installment = getInt(object, "igmpy_installment");
                    Integer jsy_installment = getInt(object, "jsy_installment");
                    Integer rajshri_installment = getInt(object, "rajshri_installment");
                    String husband_name = getString(object, "husband_father_name");
                    String mobile_no = getString(object, "mobile_no");
                    String husband_mobile_no = getString(object, "husband_mobile_no");
                    String caste = getString(object, "caste");
                    String economic_status = getString(object, "economic_status");
                    String pcts_id = getString(object, "pcts_id");
                    String bahamashah_id = getString(object, "bahamashah_or_ack_id");
                    String is_counseling_provided = getString(object, "is_counseling_prov");
                    String is_active = getString(object, "is_active");
                    Integer counseling_sms = getInt(object, "counseling_sms");


                    Integer created_by = getInt(object, "created_by");
                    bmodel.setBeneficiaryId(id);
                    bmodel.setAwcCode(awc_code);
                    bmodel.setName(name);
                    bmodel.setAge(age);
                    bmodel.setDob(AppDateTimeUtils.convertDateFromServer(dob));
                    bmodel.setStage(stage);
                    bmodel.setSubStage(sub_stage);
                    bmodel.setChildCount(no_of_child);
                    bmodel.setPmmvyInstallment(pmmvy_installment);
                    bmodel.setIgmpyInstallment(igmpy_installment);
                    bmodel.setJsyInstallment(jsy_installment);
                    bmodel.setRajshriInstallment(rajshri_installment);
                    bmodel.setHusbandName(husband_name);
                    bmodel.setHusbandMobNo(husband_mobile_no);
                    bmodel.setMobileNo(mobile_no);
                    bmodel.setCaste(caste);
                    bmodel.setEconomic(economic_status);
                    bmodel.setPctsId(pcts_id);
                    bmodel.setBahamashahId(bahamashah_id);
                    bmodel.setCounselingProv(HUtil.convYtoYes(is_counseling_provided));
                    bmodel.setCounselingSms(counseling_sms);
                    bmodel.setCreatedBy(created_by);
                    bmodel.setIsActive(is_active);
                    bmodel.setDataStatus(DataStatus.OLD);
                    beneficiaryModels.add(bmodel);


                }

                for (int i = 0; i < pregnantlist.size(); i++) {
                    JsonObject object = (JsonObject) pregnantlist.get(i);

                    PregnantEntity pmodel = new PregnantEntity();

                    String lmp_date = getString(object, "lmp_date");
                    int beneficiary_id = getInt(object, "beneficiary_id");
                    long pregnancy_id = getLong(object, "id");
                    String is_active = getString(object, "is_active");
                    pmodel.setPregnancyId(pregnancy_id);
                    pmodel.setLmpDate(AppDateTimeUtils.convertDateFromServer(lmp_date));
                    pmodel.setBeneficiaryId(beneficiary_id);
                    pmodel.setIsActive(is_active);
                    pmodel.setDataStatus(DataStatus.OLD);
                    pregnantModels.add(pmodel);

                }

                for (int i = 0; i < pw_monitorlist.size(); i++) {
                    JsonObject object = (JsonObject) pw_monitorlist.get(i);

                    PWMonitorEntity pwmodel = new PWMonitorEntity();
                    int id = getInt(object, "id");
                    int pregnancy_id = getInt(object, "pregnancy_id");
                    String stage = getString(object, "stage");
                    String sub_stage = getString(object, "sub_stage");
                    Integer anc_count = getInt(object, "anc_count");
                    String last_anc = getString(object, "last_anc");
                    Double last_weight_in_mamta = getDouble(object, "last_weight_in_mamta");
                    String last_weight_check_date = getString(object, "last_weight_check_date");
                    Double current_weight = getDouble(object, "current_weight");
                    Integer pmmvy_installment = getInt(object, "pmmvy_installment");
                    Integer igmpy_installment = getInt(object, "igmpy_installment");
                    Integer jsy_installment = getInt(object, "jsy_installment");
                    Integer rajshri_installment = getInt(object, "rajshri_installment");
                    Integer created_by = getInt(object, "created_by");
                    String is_available = getString(object, "is_available");
                    String na_reason = getString(object, "na_reason");


                    pwmodel.setId(id);
                    pwmodel.setPregnancyId(pregnancy_id);
                    pwmodel.setStage(stage);
                    pwmodel.setSubStage(sub_stage);
                    pwmodel.setAncCount(anc_count);
                    pwmodel.setLastAnc(AppDateTimeUtils.convertDateFromServer(last_anc));
                    pwmodel.setLastWeightInMamta(last_weight_in_mamta);
                    pwmodel.setLastWeightCheckDate(AppDateTimeUtils.convertDateFromServer(last_weight_check_date));
                    pwmodel.setCurrentWeight(current_weight);
                    pwmodel.setPmmvyInstallment(pmmvy_installment);
                    pwmodel.setIgmpyInstallment(igmpy_installment);
                    pwmodel.setJsyInstallment(jsy_installment);
                    pwmodel.setRajshriInstallment(rajshri_installment);
                    pwmodel.setNaReason(na_reason);
                    pwmodel.setCreatedBy(created_by);
                    pwmodel.setDataStatus(DataStatus.OLD);
                    pwmodel.setAvailable(is_available==null?null:is_available.equalsIgnoreCase("Y"));
                    pwMonitorModels.add(pwmodel);


                }

                for (int i = 0; i < childlist.size(); i++) {
                    JsonObject object = (JsonObject) childlist.get(i);
                    ChildEntity cmodel = new ChildEntity();
                    int child_id = getInt(object, "id");
                    String dob = getString(object, "dob");
                    Integer age = getInt(object, "age");
                    String stage = getString(object, "stage");
                    String sub_stage = getString(object, "sub_stage");
                    int mother_id = getInt(object, "mother_id");
                    Integer delivery_home = getInt(object, "delivery_place_type");
                    String delivery_place = getString(object, "delivery_place");
                    Integer child_order = getInt(object, "child_order");
                    String is_active = getString(object, "is_active");


                    cmodel.setChildId(child_id);
                    cmodel.setDob(AppDateTimeUtils.convertDateFromServer(dob));
                    cmodel.setAge(age);
                    cmodel.setStage(stage);
                    cmodel.setSubStage(sub_stage);
                    cmodel.setMotherId(mother_id);
                    cmodel.setDeliveryHome(delivery_home);
                    cmodel.setDeliveryPlace(delivery_place);
                    cmodel.setChildOrder(child_order);
                    cmodel.setDataStatus(DataStatus.OLD);
                    cmodel.setIsActive(is_active);

                    childModel.add(cmodel);


                }

                for (int i = 0; i < lm_monitorlist.size(); i++) {
                    JsonObject object = (JsonObject) lm_monitorlist.get(i);

                    int id = getInt(object, "id");
                    int child_id = getInt(object, "child_id");
                    String is_first_immunization_complete = getString(object, "is_first_immunization_complete");
                    Double last_muac = getDouble(object, "last_muac");
                    String last_muac_check_date = getString(object, "last_muac_check_date");
                    Double current_muac = getDouble(object, "current_muac");
                    Double birth_weight = getDouble(object, "birth_weight");
                    Double child_weight = getDouble(object, "child_weight");
                    Double child_height = getDouble(object, "child_height");
                    Integer pmmvy_installment = getInt(object, "pmmvy_installment");
                    Integer igmpy_installment = getInt(object, "igmpy_installment");
                    Integer jsy_installment = getInt(object, "jsy_installment");
                    Integer rajshri_installment = getInt(object, "rajshri_installment");
                    int created_by = getInt(object, "created_by");
                    String stage = getString(object, "stage");
                    String sub_stage = getString(object, "sub_stage");
                    String is_available = getString(object, "is_available");
                    String na_reason = getString(object, "na_reason");


                    LMMonitorEntity lmmodel = new LMMonitorEntity();
                    lmmodel.setId(id);
                    lmmodel.setChildId(child_id);
                    lmmodel.setIsFirstImmunizationComplete(HUtil.convYtoYes(is_first_immunization_complete));
                    lmmodel.setLastMuac(last_muac);
                    lmmodel.setLastMuacCheckDate(AppDateTimeUtils.convertDateFromServer(last_muac_check_date));
                    lmmodel.setCurrentMuac(current_muac);
                    lmmodel.setBirthWeight(birth_weight);
                    lmmodel.setChildWeight(child_weight);
                    lmmodel.setChildHeight(child_height);
                    lmmodel.setPmmvyInstallment(pmmvy_installment);
                    lmmodel.setIgmpyInstallment(igmpy_installment);
                    lmmodel.setJsyInstallment(jsy_installment);
                    lmmodel.setRajshriInstallment(rajshri_installment);
                    lmmodel.setCreatedBy(created_by);
                    lmmodel.setStage(stage);
                    lmmodel.setSubStage(sub_stage);
                    lmmodel.setDataStatus(DataStatus.OLD);
                    lmmodel.setNaReason(na_reason);
                    lmmodel.setAvailable(is_available==null?null:is_available.equalsIgnoreCase("Y"));
                    lmMonitorModel.add(lmmodel);


                }

                Quintet<List<BeneficiaryEntity>, List<PregnantEntity>, List<ChildEntity>, List<PWMonitorEntity>, List<LMMonitorEntity>> quintet = new Quintet<>(beneficiaryModels, pregnantModels, childModel, pwMonitorModels, lmMonitorModel);
                return quintet;
            } catch (Exception e) {
                Log.i("dd", "bulkdownload: rr");
                e.printStackTrace();
            }

        }
        return null;
    }


    public static JsonArray convertBenfUploadJson(List<BefRel> befRels) {

        JsonArray jsonArray = new JsonArray();
        for (BefRel befRel : befRels) {
            JsonObject json = dataFor(befRel);
            if (json != null)//if null no changes
                jsonArray.add(json);
        }
        Log.i("sss", "convertBenfUploadJson: " + jsonArray.toString());
        return jsonArray;
    }

    private static JsonObject dataFor(BefRel befRel) {
        Log.i("ddd", "dataFor: " + new Gson().toJson(befRel));
        BeneficiaryEntity benfBeneficiaryEntity = befRel.beneficiaryEntity;
        JsonObject bjson = getBeneficiaryJson(benfBeneficiaryEntity);
        JsonArray pregJsonArray = new JsonArray();
        JsonArray childJsonArray = new JsonArray();
        for (PregnantEntity pregnantEntity : befRel.pregnantEntities) {

            JsonObject pregJson = getPregnantJson(pregnantEntity);
            JsonArray pwArray = new JsonArray();
            for (PWMonitorEntity pwMonitorEntity : befRel.pwMonitorEntities) {
                if (pwMonitorEntity.getPregnancyId() == pregnantEntity.getPregnancyId()) {
                    if (pwMonitorEntity.getDataStatus() != DataStatus.OLD)
                        pwArray.add(getPwJson(pwMonitorEntity, pregnantEntity.getDataStatus() == DataStatus.NEW));
                }
            }
            if (pwArray.size() != 0 || pregnantEntity.getDataStatus() != DataStatus.OLD) {
                pregJson.add("pwMonitorForms", pwArray);
                pregJsonArray.add(pregJson);
            }
        }

        bjson.add("pregnant", pregJsonArray);


        for (ChildEntity childEntity : befRel.childEntities) {
            if (childEntity.getDataStatus() == DataStatus.OLD)
                continue;
            JsonObject childJson = getChildJson(childEntity);
            JsonArray lmArray = new JsonArray();
            for (LMMonitorEntity lmMonitorEntity : befRel.lmMonitorEntities) {
                if (lmMonitorEntity.getChildId() == childEntity.getChildId()) {
                    if (lmMonitorEntity.getDataStatus() != DataStatus.OLD)
                        lmArray.add(getLmJson(lmMonitorEntity, childEntity.getDataStatus() == DataStatus.NEW));
                }
            }
            childJson.add("lmMonitorForms", lmArray);
            childJsonArray.add(childJson);
        }
        bjson.add("child", childJsonArray);

        if (benfBeneficiaryEntity.getDataStatus() == DataStatus.OLD && pregJsonArray.size() == 0 && childJsonArray.size() == 0)
            return null;
        return bjson;

    }

    private static JsonObject getBeneficiaryJson(BeneficiaryEntity beneficiaryEntity) {
        JsonObject beneficiaryobject = new JsonObject();
        if (beneficiaryEntity.getDataStatus() != DataStatus.NEW)
            beneficiaryobject.addProperty("beneficiaryId", beneficiaryEntity.getBeneficiaryId());
        beneficiaryobject.addProperty("awcCode", beneficiaryEntity.getAwcCode());
        beneficiaryobject.addProperty("name", beneficiaryEntity.getName());
        beneficiaryobject.addProperty("age", beneficiaryEntity.getAge());
        beneficiaryobject.addProperty("dob", AppDateTimeUtils.convertServerDate(beneficiaryEntity.getDob()));
        beneficiaryobject.addProperty("stage", beneficiaryEntity.getStage());
        beneficiaryobject.addProperty("subStage", beneficiaryEntity.getSubStage());
        beneficiaryobject.addProperty("childCount", beneficiaryEntity.getChildCount());
        beneficiaryobject.addProperty("pmmvyInstallment", beneficiaryEntity.getPmmvyInstallment());
        beneficiaryobject.addProperty("igmpyInstallment", beneficiaryEntity.getIgmpyInstallment());
        beneficiaryobject.addProperty("jsyInstallment", beneficiaryEntity.getJsyInstallment());
        beneficiaryobject.addProperty("rajshriInstallment", beneficiaryEntity.getRajshriInstallment());
        beneficiaryobject.addProperty("husbandName", beneficiaryEntity.getHusbandName());
        beneficiaryobject.addProperty("mobileNo", beneficiaryEntity.getMobileNo());
        beneficiaryobject.addProperty("husbandMobNo", beneficiaryEntity.getHusbandMobNo());
        beneficiaryobject.addProperty("caste", beneficiaryEntity.getCaste());
        beneficiaryobject.addProperty("economic", beneficiaryEntity.getEconomic());
        beneficiaryobject.addProperty("pctsId", beneficiaryEntity.getPctsId());
        beneficiaryobject.addProperty("bahamashahId", beneficiaryEntity.getBahamashahId());
        beneficiaryobject.addProperty("counselingProv", HUtil.convYestoY(beneficiaryEntity.getCounselingProv()));
        beneficiaryobject.addProperty("counselingSms", beneficiaryEntity.getCounselingSms());
        beneficiaryobject.addProperty("isActive", beneficiaryEntity.getIsActive());
        beneficiaryobject.addProperty("createdBy", beneficiaryEntity.getCreatedBy());
        beneficiaryobject.addProperty("createdAt", beneficiaryEntity.getCreatedAt());
        beneficiaryobject.addProperty("updatedAt", beneficiaryEntity.getUpdatedAt());


        beneficiaryobject.addProperty("dataStatus", beneficiaryEntity.getDataStatus() == DataStatus.NEW ? "NEW" : "EDIT");

        return beneficiaryobject;
    }

    private static JsonObject getPregnantJson(PregnantEntity pregnantEntity) {
        JsonObject pregnantobject = new JsonObject();
        if (pregnantEntity.getDataStatus() != DataStatus.NEW)
            pregnantobject.addProperty("pregnancyId", pregnantEntity.getPregnancyId());
        pregnantobject.addProperty("beneficiaryId", pregnantEntity.getBeneficiaryId());
        pregnantobject.addProperty("lmpDate", AppDateTimeUtils.convertServerDate(pregnantEntity.getLmpDate()));
        pregnantobject.addProperty("isActive", pregnantEntity.getIsActive());
        pregnantobject.addProperty("createdAt", pregnantEntity.getCreatedAt());
        pregnantobject.addProperty("updatedAt", pregnantEntity.getUpdatedAt());
        pregnantobject.addProperty("dataStatus", pregnantEntity.getDataStatus() == DataStatus.NEW ? "NEW" : "EDIT");
        return pregnantobject;
    }

    private static JsonObject getChildJson(ChildEntity childEntity) {
        JsonObject childobject = new JsonObject();
        if (childEntity.getDataStatus() != DataStatus.NEW) {
            childobject.addProperty("childId", childEntity.getChildId());
            childobject.addProperty("motherId", childEntity.getMotherId());
        }
        childobject.addProperty("dob", AppDateTimeUtils.convertServerDate(childEntity.getDob()));
        childobject.addProperty("age", childEntity.getAge());
        childobject.addProperty("stage", childEntity.getStage());
        childobject.addProperty("subStage", childEntity.getSubStage());
        childobject.addProperty("deliveryPlaceType", childEntity.getDeliveryHome());
        childobject.addProperty("deliveryPlace", childEntity.getDeliveryPlace());
        childobject.addProperty("childOrder", childEntity.getChildOrder());
        childobject.addProperty("isActive", childEntity.getIsActive());
        childobject.addProperty("createdAt", childEntity.getCreatedAt());
        childobject.addProperty("updatedAt", childEntity.getUpdatedAt());
        childobject.addProperty("dataStatus", childEntity.getDataStatus() == DataStatus.NEW ? "NEW" : "EDIT");
        return childobject;
    }

    private static JsonObject getLmJson(LMMonitorEntity lmMonitorEntity, boolean isNew) {
        JsonObject lmObject = new JsonObject();
        if (!isNew) {
            lmObject.addProperty("childId", lmMonitorEntity.getChildId());
        }
        if (lmMonitorEntity.getDataStatus() != DataStatus.NEW)
            lmObject.addProperty("lmId", lmMonitorEntity.getId());
        lmObject.addProperty("isFirstImmunizationComplete", HUtil.convYestoY(lmMonitorEntity.getIsFirstImmunizationComplete()));
        lmObject.addProperty("lastMuac", lmMonitorEntity.getLastMuac());
        lmObject.addProperty("lastMuacCheckDate", AppDateTimeUtils.convertServerDate(lmMonitorEntity.getLastMuacCheckDate()));
        lmObject.addProperty("currentMuac", lmMonitorEntity.getCurrentMuac());
        lmObject.addProperty("birthWeight", lmMonitorEntity.getBirthWeight());
        lmObject.addProperty("childWeight", lmMonitorEntity.getChildWeight());
        lmObject.addProperty("childHeight", lmMonitorEntity.getChildHeight());
        lmObject.addProperty("pmmvyInstallment", lmMonitorEntity.getPmmvyInstallment());
        lmObject.addProperty("igmpyInstallment", lmMonitorEntity.getIgmpyInstallment());
        lmObject.addProperty("jsyInstallment", lmMonitorEntity.getJsyInstallment());
        lmObject.addProperty("rajshriInstallment", lmMonitorEntity.getRajshriInstallment());
        lmObject.addProperty("createdBy", lmMonitorEntity.getCreatedBy());
        lmObject.addProperty("stage", lmMonitorEntity.getStage());
        lmObject.addProperty("subStage", lmMonitorEntity.getSubStage());
        lmObject.addProperty("isAvailable", lmMonitorEntity.getAvailable() != null && lmMonitorEntity.getAvailable() ? "Y" : "N");
        lmObject.addProperty("naReason", lmMonitorEntity.getNaReason());
        lmObject.addProperty("createdAt", lmMonitorEntity.getCreatedAt());
        lmObject.addProperty("updatedAt", lmMonitorEntity.getUpdatedAt());
        lmObject.addProperty("dataStatus", lmMonitorEntity.getDataStatus() == DataStatus.NEW ? "NEW" : "EDIT");


        return lmObject;
    }

    private static JsonObject getPwJson(PWMonitorEntity pwMonitorEntity, boolean isNew) {
        JsonObject pwobject = new JsonObject();
        if (!isNew)
            pwobject.addProperty("pregnancyId", pwMonitorEntity.getPregnancyId());
        if (pwMonitorEntity.getDataStatus() != DataStatus.NEW)
            pwobject.addProperty("pwId", pwMonitorEntity.getId());

        pwobject.addProperty("stage", pwMonitorEntity.getStage());
        pwobject.addProperty("subStage", pwMonitorEntity.getSubStage());
        pwobject.addProperty("ancCount", pwMonitorEntity.getAncCount());
        pwobject.addProperty("lastAnc", AppDateTimeUtils.convertServerDate(pwMonitorEntity.getLastAnc()));
        pwobject.addProperty("lastWeightInMamta", pwMonitorEntity.getLastWeightInMamta());
        pwobject.addProperty("lastWeightCheckDate", AppDateTimeUtils.convertServerDate(pwMonitorEntity.getLastWeightCheckDate()));
        pwobject.addProperty("currentWeight", pwMonitorEntity.getCurrentWeight());
        pwobject.addProperty("pmmvyInstallment", pwMonitorEntity.getPmmvyInstallment());
        pwobject.addProperty("igmpyInstallment", pwMonitorEntity.getIgmpyInstallment());
        pwobject.addProperty("jsyInstallment", pwMonitorEntity.getJsyInstallment());
        pwobject.addProperty("rajshriInstallment", pwMonitorEntity.getRajshriInstallment());
        pwobject.addProperty("isAvailable", pwMonitorEntity.getAvailable() != null && pwMonitorEntity.getAvailable() ? "Y" : "N");
        pwobject.addProperty("naReason", pwMonitorEntity.getNaReason());
        pwobject.addProperty("createdBy", pwMonitorEntity.getCreatedBy());
        pwobject.addProperty("createdAt", pwMonitorEntity.getCreatedAt());
        pwobject.addProperty("updatedAt", pwMonitorEntity.getUpdatedAt());
        pwobject.addProperty("dataStatus", pwMonitorEntity.getDataStatus() == DataStatus.NEW ? "NEW" : "EDIT");

        return pwobject;
    }

}
