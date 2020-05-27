package in.rajpusht.pc.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = BeneficiaryChildEntity.TABLE)
public class BeneficiaryChildEntity {
    public static final String TABLE = "beneficiary_child";

    @PrimaryKey
    private int id;
    private String name;
    private int motherId;
    private String dob;
    private String stage;
    private String subStage;
    private String deliveryPlaceType;
    private String deliveryPlace;


}
