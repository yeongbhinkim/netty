package org.example.netty;

import java.net.InetSocketAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GlobalEventExecutor;

public class EchoServer {

    private static final int SERVER_PORT = 11011;

    private final ChannelGroup allChannels = new DefaultChannelGroup("server", GlobalEventExecutor.INSTANCE);
    private EventLoopGroup bossEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;

    public void startServer() {

        //NIO기반 EventLoopGroup 생성
        //bossEventLoopGroup은 서버 소켓을 listen할 것이고
        bossEventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        //여기서 만들어진 Channel에서 넘어온 데이터는 workerEventLoopGroup에서 처리된다.
        workerEventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("worker"));

        // Boss Thread는 ServerSocket을 Listen
        // Worker Thread는 만들어진 Channel에서 넘어온 이벤트를 처리
        //netty 서버를 생성하기 위한 헬퍼 클래스인 ServerBootstrap 인스턴스를 생성
        ServerBootstrap bootstrap = new ServerBootstrap();

        // EventLoopGroup 할당
        //만들어둔 EventLoopGroup의 group()메소드로 셋팅
        //채널을 생성 할때 NIO 소켓을 이용한 채널을 생성하도록 channel() 메소드에 NioServerSocketChannel.class를 인자로 넘겨준다
        bootstrap.group(bossEventLoopGroup, workerEventLoopGroup);

        // Channel 생성시 사용할 클래스 (NIO 소켓을 이용한 채널)
        bootstrap.channel(NioServerSocketChannel.class);

        // accept 되어 생성되는 TCP Channel 설정 childOption()
        //TCP_NODELAY, SO_KEEPALIVE 설정은 이 서버 소켓으로 연결되어 생성되는 Connection의 특성
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        // Client Request를 처리할 Handler 등록
        //채널 파이프라인을 설정하기 위해 EchoServerInitializer 객체할당
        //서버 소켓에 연결이 들어오면 이 객체가 호출되어 소켓 채널을 초기화
        bootstrap.childHandler(new EchoServerInitializer());

        try {
            // Channel 생성후 기다림
            //bind() 메소드로 소버 소켓에 포트를 바인딩
            ChannelFuture bindFuture = bootstrap.bind(new InetSocketAddress(SERVER_PORT)).sync();
            Channel channel = bindFuture.channel();
            allChannels.add(channel);

            // Channel이 닫힐 때까지 대기
            //.sync() 메소드를 호출해서 바인딩이 완료될 때까지 기다린다 이 코지가 지나가면 서버가 시작된다.
            bindFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    private void close() {

        allChannels.close().awaitUninterruptibly();
        workerEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
        bossEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
    }

    public static void main(String[] args) throws Exception {
        new EchoServer().startServer();
    }
}