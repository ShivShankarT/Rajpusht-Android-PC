package in.rajpusht.pc.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = BeneficiaryEntity.TABLE)
public class BeneficiaryEntity {
    public static final String TABLE = "beneficiary";

    @PrimaryKey
    private int id;
    private String name;
    private String husbandName;
    private String mobileNo;
    private String husbandMobNo;
    private String lmpDate;
    private String stage;
    private String subStage;
    @Ignore
    private String currentSubStage;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getHusbandMobNo() {
        return husbandMobNo;
    }

    public void setHusbandMobNo(String husbandMobNo) {
        this.husbandMobNo = husbandMobNo;
    }

    public String getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(String lmpDate) {
        this.lmpDate = lmpDate;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getSubStage() {
        return subStage;
    }

    public void setSubStage(String subStage) {
        this.subStage = subStage;
    }

    public String getCurrentSubStage() {
        return currentSubStage;
    }

    public void setCurrentSubStage(String currentSubStage) {
        this.currentSubStage = currentSubStage;
    }
}
