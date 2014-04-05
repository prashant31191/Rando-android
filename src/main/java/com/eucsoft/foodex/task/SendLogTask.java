package com.eucsoft.foodex.task;

import com.eucsoft.foodex.App;
import com.eucsoft.foodex.Constants;
import com.eucsoft.foodex.api.API;
import com.eucsoft.foodex.log.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class SendLogTask extends BaseTask {

    @Override
    public Integer run() {
        try {
            Log.d(SendLogTask.class, "Try send logs to server");
            File logFile = new File(App.context.getExternalCacheDir(), Constants.LOG_FILE_NAME);
            FileReader fileReader = new FileReader(logFile);
            BufferedReader bufferedReader = new BufferedReader((fileReader));
            StringBuilder logs = new StringBuilder("{\"log\":\"");
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                logs.append(line);
            }
            logs.append("\"}");
            bufferedReader.close();
            Log.d(SendLogTask.class, "Log file read");
            logFile.delete();
            Log.d(SendLogTask.class, "Log file deleted");

            Log.d(SendLogTask.class, "Try upload log file to server");
            API.uploadLog(logs.toString());
        } catch (Exception e) {
            Log.e(SendLogTask.class, "Can not send log, because: ", e.getMessage());
        }
        return OK;
    }
}
