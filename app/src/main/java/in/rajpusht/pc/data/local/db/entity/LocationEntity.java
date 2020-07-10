package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = LocationEntity.TABLE_NAME)
public class LocationEntity {
    public static final String TABLE_NAME = "beneficiary_location";
    @PrimaryKey
    private int id;
    private long beneficiaryId;
    private String uuid;
    private int type;
    private String gpsLocation;
    private String networkParam;
    private String createdAt;

    public LocationEntity(String uuid, int type) {
        this.uuid = uuid;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getNetworkParam() {
        return networkParam;
    }

    public void setNetworkParam(String networkParam) {
        this.networkParam = networkParam;
    }

    public long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
