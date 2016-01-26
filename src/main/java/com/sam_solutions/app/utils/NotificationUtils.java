package com.sam_solutions.app.utils;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import javafx.util.Pair;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Sends notifications via sms.
 */
@Component
public class NotificationUtils extends Thread {
    private static final Logger logger = Logger.getLogger(NotificationUtils.class);

    private String PROPERTIES_FILENAME = File.separator +
            "properties" + File.separator + "twilio.properties";

    private Queue<Pair<String, String>> taskQueue = new ConcurrentLinkedQueue<>();

    private Lock queueLock = new ReentrantLock();
    private Condition hasTask  = queueLock.newCondition();

    private String accountSid;
    private String authToken;
    private String phoneNumber;

    /**
     * Initializes twilio properties.
     */
    public NotificationUtils() {
        this.setName("NotificationUtils");
        Properties properties = new Properties();
        InputStream input = null;
        try {
            File classPath = new File(this.getClass().getClassLoader().getResource("").getPath());
            String propertiesFilePath = classPath.getAbsolutePath() + PROPERTIES_FILENAME;
            input = new FileInputStream(propertiesFilePath);
            properties.load(input);

            accountSid = properties.getProperty("twilio.account_sid");
            authToken = properties.getProperty("twilio.auth_token");
            phoneNumber = properties.getProperty("twilio.phone_number");
        } catch (IOException e) {
            logger.error("Error trying open twilio properties file");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    String error = MessageFormat.format("Error while opening properties file: {0}", e.getCause());
                    logger.error(error);
                    return;
                }
            }
        }
    }

    /**
     * Main notifications loop.
     */
    @Override
    public void run() {
        while (true) {
            while (true) {
                queueLock.lock();
                try {
                    if (taskQueue.isEmpty())
                        hasTask.await();
                } catch (InterruptedException e) {
                    String warning = MessageFormat.format("Notifications thread was interrupted: {0}", e.getCause());
                    logger.warn(warning);
                }
                queueLock.unlock();
                if (!taskQueue.isEmpty())
                    break;
            }

            while (!taskQueue.isEmpty()) {
                Pair<String, String> task = taskQueue.poll();
                sendNotification(task.getKey(), task.getValue());
            }
        }
    }

    /**
     * Sends notification to passed number.
     * @param userNumber user phone number.
     * @param notification notification to be sent.
     */
    private void sendNotification(String userNumber, String notification) {
        TwilioRestClient client = new TwilioRestClient(accountSid, authToken);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("To", userNumber));
        params.add(new BasicNameValuePair("From", phoneNumber));
        params.add(new BasicNameValuePair("Body", notification));

        SmsFactory smsFactory  = client.getAccount().getSmsFactory();
        String error = null;
        try {
            smsFactory.create(params);
        } catch (TwilioRestException e) {
            error = MessageFormat.format("Error while trying to send " +
                    "notification to {0}: {1}", userNumber, e.getCause());
            logger.error(error);
        }
        if (error == null) {
            String message = MessageFormat.format("Notification was " +
                    "successfully send to {0}", userNumber);
            logger.info(message);
        }
    }

    /**
     * Notifies users.
     * @param phoneNumbers user phone numbers.
     * @param notification notification message text.
     */
    public void notifyUsers(List<String> phoneNumbers, String notification) {
        if (phoneNumbers.isEmpty())
            return;

        for (String phoneNumber : phoneNumbers) {
            taskQueue.add(new Pair<>(phoneNumber, notification));
        }

        queueLock.lock();
        hasTask.signal();
        queueLock.unlock();
    }
}
