package org.example.netty;

import java.net.InetSocketAddress;
import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

public class Client {
    private static final int SERVER_PORT = 11011;
    private final String host;
    private final int port;

    private Channel serverChannel;
    private EventLoopGroup eventLoopGroup;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws InterruptedException {

        //클라이언트라서 서버소켓에 listen하기 위한 boss 그룹은 없다.
        eventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("client"));

        Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup);

       
        bootstrap.channel(NioSocketChannel.class);
        //remoteAddress() 메소드로 접속할 서버 소켓의 주소와 포트를 입력
        bootstrap.remoteAddress(new InetSocketAddress(host, port));
        //handler() 메소드로 ClientInitializer()를 넘겨준다
        bootstrap.handler(new ClientInitializer());

        //connect() 메소드로 서버 소켓에 연결을 하고 sync()로 기다린다.
        serverChannel = bootstrap.connect().sync().channel();
    }

    private void start() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        String message;
        ChannelFuture future;

        while(true) {
            // 사용자 입력
            message = scanner.nextLine();

            // Server로 전송
            future = serverChannel.writeAndFlush(message.concat("\n"));

            if("quit".equals(message)){
                serverChannel.closeFuture().sync();
                break;
            }
        }

        // 종료되기 전 모든 메시지가 flush 될때까지 기다림
        if(future != null){
            future.sync();
        }
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client("127.0.0.1", SERVER_PORT);

        try {
            client.connect();
            client.start();
        } finally {
            client.close();
        }
    }

}
