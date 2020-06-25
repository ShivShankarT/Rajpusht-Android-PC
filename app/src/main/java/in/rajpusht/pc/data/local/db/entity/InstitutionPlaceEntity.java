package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = InstitutionPlaceEntity.TABLE_NAME)
public class InstitutionPlaceEntity {

    public static final String TABLE_NAME = "institution_place";
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("District")
    private String district;
    @SerializedName("Block")
    private String block;
    @SerializedName("Type of facility")
    private String typeOfFacility;
    @SerializedName("Location")
    private String location;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getTypeOfFacility() {
        return typeOfFacility;
    }

    public void setTypeOfFacility(String typeOfFacility) {
        this.typeOfFacility = typeOfFacility;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
