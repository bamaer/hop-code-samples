package org.apache.hop.dummy;

import org.apache.hop.core.row.RowMetaInterface;
import org.apache.hop.trans.step.BaseStepData;
import org.apache.hop.trans.step.StepDataInterface;

public class DummyActionData extends BaseStepData implements StepDataInterface {
    public RowMetaInterface outputRowMeta;

    public DummyActionData() {
        super();
    }
}