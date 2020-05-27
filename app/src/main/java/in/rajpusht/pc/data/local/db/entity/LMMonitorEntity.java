package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import in.rajpusht.pc.model.DataStatus;

@Entity(tableName = LMMonitorEntity.TABLE)
public class LMMonitorEntity {
    public static final String TABLE = "lm_monitor";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long childId;
    private String stage;
    private String subStage;
    private String isFirstImmunizationComplete;
    private Double lastMuac;
    private Date lastMuacCheckDate;
    private Double currentMuac;
    private Double birthWeight;
    private Double childWeight;
    private Integer pmmvyInstallmentCt;
    private Integer igmpyInstallmentCt;
    private Integer jsyInstallmentCt;
    private Integer rajshriInstallmentCt;
    private String timestamp;
    private DataStatus dataStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getChildId() {
        return childId;
    }

    public void setChildId(long childId) {
        this.childId = childId;
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

    public String getIsFirstImmunizationComplete() {
        return isFirstImmunizationComplete;
    }

    public void setIsFirstImmunizationComplete(String isFirstImmunizationComplete) {
        this.isFirstImmunizationComplete = isFirstImmunizationComplete;
    }

    public Double getLastMuac() {
        return lastMuac;
    }

    public void setLastMuac(Double lastMuac) {
        this.lastMuac = lastMuac;
    }

    public Date getLastMuacCheckDate() {
        return lastMuacCheckDate;
    }

    public void setLastMuacCheckDate(Date lastMuacCheckDate) {
        this.lastMuacCheckDate = lastMuacCheckDate;
    }

    public Double getCurrentMuac() {
        return currentMuac;
    }

    public void setCurrentMuac(Double currentMuac) {
        this.currentMuac = currentMuac;
    }

    public Double getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(Double birthWeight) {
        this.birthWeight = birthWeight;
    }

    public Double getChildWeight() {
        return childWeight;
    }

    public void setChildWeight(Double childWeight) {
        this.childWeight = childWeight;
    }

    public Integer getPmmvyInstallmentCt() {
        return pmmvyInstallmentCt;
    }

    public void setPmmvyInstallmentCt(Integer pmmvyInstallmentCt) {
        this.pmmvyInstallmentCt = pmmvyInstallmentCt;
    }

    public Integer getIgmpyInstallmentCt() {
        return igmpyInstallmentCt;
    }

    public void setIgmpyInstallmentCt(Integer igmpyInstallmentCt) {
        this.igmpyInstallmentCt = igmpyInstallmentCt;
    }

    public Integer getJsyInstallmentCt() {
        return jsyInstallmentCt;
    }

    public void setJsyInstallmentCt(Integer jsyInstallmentCt) {
        this.jsyInstallmentCt = jsyInstallmentCt;
    }

    public Integer getRajshriInstallmentCt() {
        return rajshriInstallmentCt;
    }

    public void setRajshriInstallmentCt(Integer rajshriInstallmentCt) {
        this.rajshriInstallmentCt = rajshriInstallmentCt;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public DataStatus getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(DataStatus dataStatus) {
        this.dataStatus = dataStatus;
    }
}
