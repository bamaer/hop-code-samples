package org.apache.hop.dummy;

import org.apache.hop.core.CheckResult;
import org.apache.hop.core.CheckResultInterface;
import org.apache.hop.core.Const;
import org.apache.hop.core.annotations.Step;
import org.apache.hop.core.exception.HopException;
import org.apache.hop.core.exception.HopXMLException;
import org.apache.hop.core.row.RowMetaInterface;
import org.apache.hop.core.row.ValueMetaAndData;
import org.apache.hop.core.row.ValueMetaInterface;
import org.apache.hop.core.row.value.ValueMetaNumber;
import org.apache.hop.core.variables.VariableSpace;
import org.apache.hop.core.xml.XMLHandler;
import org.apache.hop.metastore.api.IMetaStore;
import org.apache.hop.trans.Trans;
import org.apache.hop.trans.TransMeta;
import org.apache.hop.trans.step.*;
import org.w3c.dom.*;

import java.util.List;

/*
 * Created on 02-jun-2003
 *
 */

@Step( id = "HopDummySample",
        image = "dummy.svg",
        i18nPackageName = "org.apache.hop.dummy",
        name = "Dummy.Action.Name",
        description = "Dummy.Action.Description",
        categoryDescription = "i18n:org.apache.hop.trans.step:BaseStep.Category.Flow"
//        suggestion = "DummyAction.Step.SuggestedStep"
        )
public class DummyActionMeta extends BaseStepMeta implements StepMetaInterface {
    private ValueMetaAndData value;

    public DummyActionMeta() {
        super(); // allocate BaseStepInfo
    }

    /**
     * @return Returns the value.
     */
    public ValueMetaAndData getValue() {
        return value;
    }

    /**
     * @param value The value to set.
     */
    public void setValue( ValueMetaAndData value ) {
        this.value = value;
    }

    @Override
    public String getXML() throws HopException {
        String retval = "";

        retval += "    <values>" + Const.CR;
        if ( value != null ) {
            retval += value.getXML();
        }
        retval += "      </values>" + Const.CR;

        return retval;
    }

    @Override
    public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space, IMetaStore metaStore ) {
        if ( value != null ) {
            ValueMetaInterface v = value.getValueMeta();
            v.setOrigin( origin );

            r.addValueMeta( v );
        }
    }

    @Override
    public Object clone() {
        Object retval = super.clone();
        return retval;
    }

    @Override
    public void loadXML(Node stepnode, IMetaStore metaStore) throws HopXMLException {
        try {
            value = new ValueMetaAndData();

            Node valnode = XMLHandler.getSubNode( stepnode, "values", "value" );
            if ( valnode != null ) {
                System.out.println( "reading value in " + valnode );
                value.loadXML( valnode );
            }
        } catch ( Exception e ) {
            throw new HopXMLException( "Unable to read step info from XML node", e );
        }
    }

    @Override
    public void setDefault() {
        value = new ValueMetaAndData( new ValueMetaNumber( "valuename" ), new Double( 123.456 ) );
        value.getValueMeta().setLength( 12 );
        value.getValueMeta().setPrecision( 4 );
    }

    @Override
    public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info, VariableSpace space, IMetaStore metaStore ) {
        CheckResult cr;
        if ( prev == null || prev.size() == 0 ) {
            cr = new CheckResult( CheckResult.TYPE_RESULT_WARNING, "Not receiving any fields from previous steps!", stepMeta );
            remarks.add( cr );
        } else {
            cr = new CheckResult( CheckResult.TYPE_RESULT_OK, "Step is connected to previous one, receiving " + prev.size() + " fields", stepMeta );
            remarks.add( cr );
        }

        // See if we have input streams leading to this step!
        if ( input.length > 0 ) {
            cr = new CheckResult( CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta );
            remarks.add( cr );
        } else {
            cr = new CheckResult( CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta );
            remarks.add( cr );
        }
    }

//    public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name ) {
//        return new DummyActionDialog( shell, meta, transMeta, name );
//    }

    @Override
    public String getDialogClassName(){
        return DummyActionDialog.class.getCanonicalName();
    }

    @Override
    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp ) {
        return new DummyAction( stepMeta, stepDataInterface, cnr, transMeta, disp );
    }

    @Override
    public StepDataInterface getStepData() {
        return new DummyActionData();
    }
}