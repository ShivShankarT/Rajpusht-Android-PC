package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

import in.rajpusht.pc.model.DataStatus;


@Entity(tableName = PregnantEntity.TABLE)
public class PregnantEntity {
    public static final String TABLE = "pregnant";
    @Ignore
    List<PWMonitorEntity> pwMonitorEntities;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long beneficiaryId;
    private long pregnancyId;
    private Date lmpDate;
    private DataStatus dataStatus;
    private String createdAt;
    private String updatedAt;
    private boolean isNew;


    public long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public long getPregnancyId() {
        return pregnancyId;
    }

    public void setPregnancyId(long pregnancyId) {
        this.pregnancyId = pregnancyId;
    }

    public Date getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(Date lmpDate) {
        this.lmpDate = lmpDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DataStatus getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(DataStatus dataStatus) {
        this.dataStatus = dataStatus;
    }

    public List<PWMonitorEntity> getPwMonitorEntities() {
        return pwMonitorEntities;
    }

    public void setPwMonitorEntities(List<PWMonitorEntity> pwMonitorEntities) {
        this.pwMonitorEntities = pwMonitorEntities;
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

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}