package com.example.davidgeisinger.tennistracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class VoiceRecognitionService extends Service
{
    protected AudioManager mAudioManager;
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected final Messenger mServerMessenger = new Messenger(new IncomingHandler(this));
    protected Messenger mResponseMessenger;

    protected boolean mIsListening;

    static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CONTINUE_LISTENING = 2;
    static final int MSG_RECOGNIZER_CANCEL = 3;
    static final int MSG_IN = 4;
    static final int MSG_LONG = 5;
    static final int MSG_WIDE = 6;
    static final int MSG_NET = 7;


    @Override
    public void onCreate() {
        super.onCreate();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
    }

    protected static class IncomingHandler extends Handler
    {
        private WeakReference<VoiceRecognitionService> mtarget;

        IncomingHandler(VoiceRecognitionService target)
        {
            mtarget = new WeakReference<VoiceRecognitionService>(target);
        }


        @Override
        public void handleMessage(Message msg)
        {
            final VoiceRecognitionService target = mtarget.get();

            switch (msg.what) {
                case MSG_RECOGNIZER_START_LISTENING:
                    target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                    target.mIsListening = true;
                    target.mResponseMessenger = msg.replyTo;
                    Log.d("Recgonizer", "message start listening"); //$NON-NLS-1$
                    break;
                case MSG_RECOGNIZER_CONTINUE_LISTENING:
                    if (!target.mIsListening) {
                        target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                        target.mIsListening = true;
                        Log.d("Recgonizer", "message continue listening"); //$NON-NLS-1$
                    }
                    break;

                case MSG_RECOGNIZER_CANCEL:
                    target.mSpeechRecognizer.cancel();
                    target.mIsListening = false;
                    Log.d("Recognizer", "message canceled recognizer"); //$NON-NLS-1$
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d("Destroy", "message canceled recognizer");
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
        super.onDestroy();

    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        private static final String TAG = "SpeechRecognition";

        @Override
        public void onBeginningOfSpeech() {
            //Log.d(TAG, "onBeginingOfSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            //Log.d(TAG, "onEndOfSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onError(int error)
        {
            mIsListening = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_CONTINUE_LISTENING);
            try
            {
                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {

            }
            Log.d(TAG, "error = " + error); //$NON-NLS-1$
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results) {
            Log.d(TAG, "onResults"); //$NON-NLS-1$
            ArrayList strlist = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            Message res = new Message();
            for (int i = 0; i < strlist.size();i++ ) {
                Log.d("Speech", "result=" + strlist.get(i));
                switch(strlist.get(i).toString()) {
                    case "in":
                        res.what = VoiceRecognitionService.MSG_IN;
                        try {
                            mResponseMessenger.send(res);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "long":
                        res.what = VoiceRecognitionService.MSG_LONG;
                        try {
                            mResponseMessenger.send(res);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "wide":
                        res.what = VoiceRecognitionService.MSG_WIDE;
                        try {
                            mResponseMessenger.send(res);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "net":
                        res.what = VoiceRecognitionService.MSG_NET;
                        try {
                            mResponseMessenger.send(res);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            mIsListening = false;
            Message msg = new Message();
            msg.what = VoiceRecognitionService.MSG_RECOGNIZER_CONTINUE_LISTENING;
            try {
                mServerMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("BINDED", "onBind");
        return mServerMessenger.getBinder();
    }
}
