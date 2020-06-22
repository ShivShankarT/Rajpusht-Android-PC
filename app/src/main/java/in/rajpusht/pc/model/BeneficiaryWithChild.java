package in.rajpusht.pc.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;

public class BeneficiaryWithChild {
    @Embedded
    private BeneficiaryEntity beneficiaryEntity;

    @Relation(parentColumn = "beneficiaryId", entityColumn = "motherId", entity = ChildEntity.class)
    private List<ChildEntity> childEntities;

    @Relation(parentColumn = "beneficiaryId", entityColumn = "beneficiaryId", entity = PregnantEntity.class)
    private PregnantEntity pregnantEntity;

    public BeneficiaryEntity getBeneficiaryEntity() {
        return beneficiaryEntity;
    }

    public void setBeneficiaryEntity(BeneficiaryEntity beneficiaryEntity) {
        this.beneficiaryEntity = beneficiaryEntity;
    }

    public List<ChildEntity> getChildEntities() {
        return childEntities;
    }

    public void setChildEntities(List<ChildEntity> childEntities) {
        this.childEntities = childEntities;
    }

    public PregnantEntity getPregnantEntity() {
        return pregnantEntity;
    }

    public void setPregnantEntity(PregnantEntity pregnantEntity) {
        this.pregnantEntity = pregnantEntity;
    }
}
