package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import in.rajpusht.pc.model.DataStatus;

@Entity(tableName = PWMonitorEntity.TABLE, indices = {@Index(value = "pregnancyId"), @Index(value = "beneficiaryId")})
public class PWMonitorEntity {
    public static final String TABLE = "pw_monitor";

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long pregnancyId;
    private long beneficiaryId;
    private String stage;
    private String subStage;
    private Integer ancCount;
    private Date lastAnc;
    private Double lastWeightInMamta;
    private Date lastWeightCheckDate;
    private Double currentWeight;
    private Integer pmmvyInstallment;
    private Integer igmpyInstallment;
    private Integer jsyInstallment;
    private Integer rajshriInstallment;
    private DataStatus dataStatus;
    private Boolean available;
    private String naReason;
    private Long createdBy;
    private String createdAt;
    private String updatedAt;
    private String uuid;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPregnancyId() {
        return pregnancyId;
    }

    public void setPregnancyId(long pregnancyId) {
        this.pregnancyId = pregnancyId;
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

    public Integer getAncCount() {
        return ancCount;
    }

    public void setAncCount(Integer ancCount) {
        this.ancCount = ancCount;
    }

    public Date getLastAnc() {
        return lastAnc;
    }

    public void setLastAnc(Date lastAnc) {
        this.lastAnc = lastAnc;
    }

    public Double getLastWeightInMamta() {
        return lastWeightInMamta;
    }

    public void setLastWeightInMamta(Double lastWeightInMamta) {
        this.lastWeightInMamta = lastWeightInMamta;
    }

    public Date getLastWeightCheckDate() {
        return lastWeightCheckDate;
    }

    public void setLastWeightCheckDate(Date lastWeightCheckDate) {
        this.lastWeightCheckDate = lastWeightCheckDate;
    }

    public Double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Double currentWeight) {
        this.currentWeight = currentWeight;
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

    public long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "PWMonitorEntity{" +
                "id=" + id +
                ", pregnancyId=" + pregnancyId +
                ", beneficiaryId=" + beneficiaryId +
                ", stage='" + stage + '\'' +
                ", subStage='" + subStage + '\'' +
                ", ancCount=" + ancCount +
                ", lastAnc=" + lastAnc +
                ", lastWeightInMamta=" + lastWeightInMamta +
                ", lastWeightCheckDate=" + lastWeightCheckDate +
                ", currentWeight=" + currentWeight +
                ", pmmvyInstallment=" + pmmvyInstallment +
                ", igmpyInstallment=" + igmpyInstallment +
                ", jsyInstallment=" + jsyInstallment +
                ", rajshriInstallment=" + rajshriInstallment +
                ", dataStatus=" + dataStatus +
                ", available=" + available +
                ", naReason='" + naReason + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
