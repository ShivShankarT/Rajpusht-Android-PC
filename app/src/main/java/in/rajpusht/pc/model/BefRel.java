package in.rajpusht.pc.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;

public class BefRel {

    @Embedded
    public BeneficiaryEntity beneficiaryEntity;

    @Relation(parentColumn = "beneficiaryId", entityColumn = "motherId", entity = ChildEntity.class)
    public List<ChildEntity> childEntities;

    @Relation(parentColumn = "beneficiaryId", entityColumn = "beneficiaryId", entity = PregnantEntity.class)
    public List<PregnantEntity> pregnantEntities;

    @Relation(parentColumn = "beneficiaryId", entityColumn = "beneficiaryId", entity = PWMonitorEntity.class)
    public List<PWMonitorEntity> pwMonitorEntities;

    @Relation(parentColumn = "beneficiaryId", entityColumn = "motherId", entity = LMMonitorEntity.class)
    public List<LMMonitorEntity> lmMonitorEntities;

}
