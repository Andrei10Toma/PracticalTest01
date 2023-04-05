package ro.pub.cs.systems.eim.practicaltest01;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public class ProcessingThread extends Thread {
    private Context context;
    private int sum;
    public ProcessingThread(Context context, int sum) {
        this.context = context;
        this.sum = sum;
    }

    @Override
    public void run() {
        Log.d("PracticalTest01Service", "Thread.run() was invoked, PID: " + android.os.Process.myPid() + " TID: " + android.os.Process.myTid());
        while (true) {
            sleep();
            sendMessage();
        }
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(Constants.SUM_BROADCAST);
        intent.putExtra("data", new Date(System.currentTimeMillis()).toString() + " " + sum);
        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}
