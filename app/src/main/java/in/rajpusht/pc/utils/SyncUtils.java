package in.rajpusht.pc.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.local.db.AppDatabase;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.data.remote.AppApiHelper;
import in.rajpusht.pc.model.DataStatus;

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



    public void syncToServer(AppDatabase appDatabase) {


        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();


        for (BeneficiaryEntity beneficiaryEntity : appDatabase.beneficiaryDao().getBeneficiaryNotSync(DataStatus.NEW)) {

            if (beneficiaryEntity != null) {
                List<PregnantEntity> pregnantEntities = appDatabase.pregnantDao().getPregnantById(beneficiaryEntity.getBeneficiaryId());
                List<ChildEntity> childEntities = appDatabase.childDao().childEntities(beneficiaryEntity.getBeneficiaryId());
                List<PWMonitorEntity> pwMonitorEntities = null;
                List<LMMonitorEntity> lmMonitorEntities = null;

                if (pregnantEntities != null && !pregnantEntities.isEmpty()) {

                    for (PregnantEntity pregnantEntity : pregnantEntities) {
                        pwMonitorEntities = appDatabase.pwMonitorDao().pwMonitor(pregnantEntity.getPregnancyId());
                        if (pwMonitorEntities != null && !pwMonitorEntities.isEmpty()) {
                            pregnantEntity.setPwMonitorEntities(pwMonitorEntities);
                        }
                    }
                }


                if (childEntities != null && !childEntities.isEmpty()) {
                    for (ChildEntity childEntity : childEntities) {
                        lmMonitorEntities = appDatabase.lmMonitorDao().lmMonitor(childEntity.getChildId());
                        if (lmMonitorEntities != null && !lmMonitorEntities.isEmpty()) {
                            childEntity.setLmMonitorEntities(lmMonitorEntities);
                        }
                    }
                }


                JsonObject benefJson = (JsonObject) gson.toJsonTree(beneficiaryEntity);
                JsonArray childJsonArray = (JsonArray) gson.toJsonTree(childEntities);
                JsonArray pregnantJson = (JsonArray) gson.toJsonTree(pregnantEntities);

                benefJson.add("pregnant", pregnantJson);
                benefJson.add("child", childJsonArray);


                jsonArray.add(benefJson);
            }
            JsonObject mainobject=new JsonObject();
            mainobject.addProperty("beneficiary", jsonArray.toString());

            appApiHelper.bulkUpload(mainobject);

        }



    }

}
