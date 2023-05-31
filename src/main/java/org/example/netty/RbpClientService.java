package org.example.netty;


import org.springframework.stereotype.Component;

import java.util.Scanner;


@Component
public class RbpClientService {

    private static final int SERVER_PORT = 11011;

    String reMessage;
    public RbpClientService() throws Exception {
    }


    //	public Map<String, String> doRequest(Map<String, String> Map) throws Exception{
    public void doRequest() throws Exception {
        Scanner scanner = new Scanner(System.in);
        String message;

        while (true) {
            message = scanner.nextLine();

            //IP,포트설정
            Client client = new Client("127.0.0.1", SERVER_PORT);

            try {
                client.connect();
                client.start(message);
            } finally {
                client.close();
            }
            Thread.sleep(1000);
        }


    }

    public void receiver(String message)  {

        System.out.println("==============3===============");
        reMessage = message;
        System.out.println(reMessage);
    }


    public static void main(String[] args) throws Exception {
        RbpClientService rbpClientService = new RbpClientService();
        rbpClientService.doRequest();
    }


}