package org.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 11011;
    private final String host;
    private final int port;

    private Channel serverChannel;
    //EventLoopGroup는 EventLoop들의 그룹이다 여러개의  EventLoop은 하나의 그룹으로 모아질수 있다 같은 그룹에 속한 EventLoop들은 스레드와 같은 몇몇 리소스들을 공유
    //EventLoop는 새로운 이벤트를 반복적으로 확인하는 루프다 예를들어 SocketChannel로부터 새로운 데이터가 들어오는 것 같은 이벤트를 매번 확인한다. 이벤트가 발생하면 적당한 이벤트 핸들러에 전달
    private EventLoopGroup eventLoopGroup;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws InterruptedException {

        //클라이언트라서 서버소켓에 listen하기 위한 boss 그룹은 없다.
        eventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("client"));

//      Bootstrap 클래스는 Netty의 스레드를 생성하고 소켓을 오픈하는 등 Netty를 구동하기 위한 부트스트래핑을 위해 사용하는 클래스
        Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup);

       //SocketChannel은 TCP 연결을 대표한다. 네트워크 프로그램에서 Server나 Client가 Netty를 사용한다면 머신 사이에서 데이터를 전달하는 과정은 SoketChannel을 통해서 이뤄진다.
        // SocketChannel은 항상 같은 EventLoop에 의해 관리가 된다 같은 EventLoop는 항상 같은 스레드에서 실행이 되기 때문에 SocketChannel은 항상 같은 스레드에서 접근된다
        // 이 때문에 순서가 보장된다 따라서 같은 SocketChannel에서 동시에 데이터가 읽히는 것에 대해서는 걱정하지 않아도 됩니다.
        bootstrap.channel(NioSocketChannel.class);
        //remoteAddress() 메소드로 접속할 서버 소켓의 주소와 포트를 입력
        bootstrap.remoteAddress(new InetSocketAddress(host, port));
        //handler() 메소드로 ClientInitializer()를 넘겨준다
        bootstrap.handler(new ClientInitializer());

        //connect() 메소드로 서버 소켓에 연결을 하고 sync()로 기다린다.
        serverChannel = bootstrap.connect().sync().channel();
    }

//    public void start() throws InterruptedException {
    public void start(String message) throws InterruptedException {
//        Scanner scanner = new Scanner(System.in);

//        String message;
        ChannelFuture future;

//        while(true) {
            System.out.println(message);
            // 사용자 입력
//            message = scanner.nextLine();
//            message = str;
            // Server로 전송
            future = serverChannel.writeAndFlush(message.concat("\n"));
//            future = serverChannel.writeAndFlush(str);

            if("quit".equals(message)){
                serverChannel.closeFuture().sync();
//                break;
            }
//        }

        // 종료되기 전 모든 메시지가 flush 될때까지 기다림
        if(future != null){
            future.sync();
        }

    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {

//        //IP,포트설정
//        Client client = new Client("127.0.0.1", SERVER_PORT);
//
//        try {
//            client.connect();
//            client.start();
//        } finally {
//            client.close();
//        }
    }

}
