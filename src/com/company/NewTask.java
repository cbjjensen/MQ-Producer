package com.company;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class NewTask {

    private final static String QUEUE_NAME = "Task_Queue";
    public static void main(String[] args) throws java.io.IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;
        //Durable =  make sure that RabbitMQ will never lose our queue (even if queue shuts down)

        // initiate a durable queue named QUEUE_NAME
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        String message = getJsonMessage();

        // Push a Persistent message to QUEUE_NAME
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");


        channel.close();
        connection.close();
    }

    private static String getMessage(String[] strings){
        if (strings.length < 1) {
            //Random random = new Random();
            int num = (int )(Math.random() * 5 + 1);
            String message = "";
            for(int i = 0; i<=num;i++){
                message+=".";
            }
            return message;
        }
        return joinStrings(strings, " ");
    }
    private static String getJsonMessage(){

        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Incident Number","INC1234");
        map.put("Universal Ticket Number","INC1234");
        map.put("Summary","Test");
        map.put("Description","Testtesttestestes");
        map.put("Create Date",1234);
        String json = gson.toJson(map);
        return json;
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) return "";
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }

}
