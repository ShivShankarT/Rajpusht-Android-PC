package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import in.rajpusht.pc.model.DataStatus;

@Entity(tableName = LMMonitorEntity.TABLE,indices = {@Index(value = "childId"), @Index(value = "motherId")})
public class LMMonitorEntity {
    public static final String TABLE = "lm_monitor";
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long childId;
    private long motherId;
    private String stage;
    private String subStage;
    private Double lastMuac;
    private Date lastMuacCheckDate;
    private Double currentMuac;
    private Double childWeight;
    private Double childHeight;
    private Integer pmmvyInstallment;
    private Integer igmpyInstallment;
    private Integer jsyInstallment;
    private Integer rajshriInstallment;
    private  Long createdBy;
    private String naReason;
    private Boolean available;
    private DataStatus dataStatus;
    private String createdAt;
    private String updatedAt;


    public long getId() {
        return id;
    }

    public void setId(long id) {
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


    public Double getChildWeight() {
        return childWeight;
    }

    public void setChildWeight(Double childWeight) {
        this.childWeight = childWeight;
    }

    public Integer getPmmvyInstallment() {
        return pmmvyInstallment;
    }

    public void setPmmvyInstallment(Integer pmmvyInstallment) {
        this.pmmvyInstallment = pmmvyInstallment;
    }

    public Integer getIgmpyInstallment() {
        return igmpyInstallment;
    }

    public void setIgmpyInstallment(Integer igmpyInstallment) {
        this.igmpyInstallment = igmpyInstallment;
    }

    public Integer getJsyInstallment() {
        return jsyInstallment;
    }

    public void setJsyInstallment(Integer jsyInstallment) {
        this.jsyInstallment = jsyInstallment;
    }

    public Integer getRajshriInstallment() {
        return rajshriInstallment;
    }

    public void setRajshriInstallment(Integer rajshriInstallment) {
        this.rajshriInstallment = rajshriInstallment;
    }


    public DataStatus getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(DataStatus dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public long getMotherId() {
        return motherId;
    }

    public void setMotherId(long motherId) {
        this.motherId = motherId;
    }

    public Double getChildHeight() {
        return childHeight;
    }

    public void setChildHeight(Double childHeight) {
        this.childHeight = childHeight;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getNaReason() {
        return naReason;
    }

    public void setNaReason(String naReason) {
        this.naReason = naReason;
    }


}
