package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {

    public static void log(String user, String action, String LOG_FILE_PATH) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        String logMessage = timestamp + " - 用户 " + user + " 操作: " + action + "\n";


        File logFile = new File(LOG_FILE_PATH);
        try {
            // 检查文件是否存在，不存在则创建
            if (!logFile.exists()) {
                logFile.createNewFile();
            }


            try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
                writer.println(logMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
