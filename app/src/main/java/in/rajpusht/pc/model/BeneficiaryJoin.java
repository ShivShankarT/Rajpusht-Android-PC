package in.rajpusht.pc.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;

public class BeneficiaryJoin {
    @Embedded
    private BeneficiaryEntity beneficiaryEntity;

    @Relation(parentColumn = "beneficiaryId", entityColumn = "motherId", entity = ChildEntity.class)
    private ChildEntity childEntity;

    @Relation(parentColumn = "beneficiaryId", entityColumn = "beneficiaryId", entity = PregnantEntity.class)
    private PregnantEntity pregnantEntity;

    public BeneficiaryEntity getBeneficiaryEntity() {
        return beneficiaryEntity;
    }

    public void setBeneficiaryEntity(BeneficiaryEntity beneficiaryEntity) {
        this.beneficiaryEntity = beneficiaryEntity;
    }

    public ChildEntity getChildEntity() {
        return childEntity;
    }

    public void setChildEntity(ChildEntity childEntity) {
        this.childEntity = childEntity;
    }

    public PregnantEntity getPregnantEntity() {
        return pregnantEntity;
    }

    public void setPregnantEntity(PregnantEntity pregnantEntity) {
        this.pregnantEntity = pregnantEntity;
    }
}
