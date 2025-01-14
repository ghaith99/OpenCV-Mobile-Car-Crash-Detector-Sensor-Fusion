package com.example.kwt.camera3;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

class SoundMeter {
    //This file is used to record voice

    static final private double EMA_FILTER = 0.6;

    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;

    public void start(){

        if(mRecorder == null){

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            try {
                mRecorder.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            mRecorder.start();
            mEMA = 0.0;
        }
    }

    public void stop(){
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void pause(){
        releaseMediaRecorder();
    }


    private void releaseMediaRecorder(){
        if (mRecorder != null) {
            // clear recorder configuration
            mRecorder.reset();
            // release the recorder object
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitudeDb(double amp) {
        if (mRecorder != null){
            //return  (mRecorder.getMaxAmplitude()/2700.0);
            double db =  20 * Math.log10(amp / 0.3);
            Log.i("Noise", "==== Current Noise  ===, Amplitude: "+amp + ", DB: "+db+"===");
            return db;
        }
        else
            return 0;
    }

    public double getAmplitude(){
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;
    }

    public double getAmplitudeEMA(){

        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }







}
