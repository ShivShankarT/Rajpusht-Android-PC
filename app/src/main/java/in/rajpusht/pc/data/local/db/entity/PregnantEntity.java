package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import in.rajpusht.pc.model.DataStatus;


@Entity(tableName = PregnantEntity.TABLE)
public class PregnantEntity {
    public static final String TABLE = "PregnantEntity";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long beneficiaryId;
    private long pregnancyId;
    private Date lmpDate;
    private DataStatus dataStatus;

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
}