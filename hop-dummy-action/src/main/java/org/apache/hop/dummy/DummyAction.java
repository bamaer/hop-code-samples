package org.apache.hop.dummy;


import org.apache.hop.core.Const;
import org.apache.hop.core.exception.HopException;
import org.apache.hop.core.row.RowDataUtil;
import org.apache.hop.trans.Trans;
import org.apache.hop.trans.TransMeta;
import org.apache.hop.trans.step.*;

public class DummyAction extends BaseStep implements StepInterface {
    private DummyActionData data;
    private DummyActionMeta meta;

    public DummyAction(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis ) {
        super( s, stepDataInterface, c, t, dis );
    }

    @Override
    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi ) throws HopException {
        meta = (DummyActionMeta) smi;
        data = (DummyActionData) sdi;

        Object[] r = getRow();    // get row, blocks when needed!
        if ( r == null ) { // no more input to be expected...
            setOutputDone();
            return false;
        }

        if ( first ) {
            first = false;

            data.outputRowMeta = getInputRowMeta().clone();
            meta.getFields( data.outputRowMeta, getStepname(), null, null, this, metaStore);
        }

        Object extraValue = meta.getValue().getValueData();

        Object[] outputRow = RowDataUtil.addValueData( r, data.outputRowMeta.size() - 1, extraValue );

        putRow( data.outputRowMeta, outputRow );     // copy row to possible alternate rowset(s).

        if ( checkFeedback( getLinesRead() ) ) {
            logBasic( "Linenr " + getLinesRead() );  // Some basic logging every 5000 rows.
        }

        return true;
    }

    @Override
    public boolean init(StepMetaInterface smi, StepDataInterface sdi ) {
        meta = (DummyActionMeta) smi;
        data = (DummyActionData) sdi;

        return super.init( smi, sdi );
    }

    @Override
    public void dispose( StepMetaInterface smi, StepDataInterface sdi ) {
        meta = (DummyActionMeta) smi;
        data = (DummyActionData) sdi;

        super.dispose( smi, sdi );
    }

    //
    // Run is were the action happens!
    public void run() {
        logBasic( "Starting to run..." );
        try {
            while ( processRow( meta, data ) && !isStopped() ) {
                // Process rows
            }
        } catch ( Exception e ) {
            logError( "Unexpected error : " + e.toString() );
            logError( Const.getStackTracker( e ) );
            setErrors( 1 );
            stopAll();
        } finally {
            dispose( meta, data );
            logBasic( "Finished, processing " + getLinesRead() + " rows" );
            markStop();
        }
    }
}