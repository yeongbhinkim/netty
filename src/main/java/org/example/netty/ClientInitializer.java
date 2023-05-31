package org.example.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * ChannelIntializer는 SocketChannel이 생성될 때 ChannelPipeline에 추가되는 특별한 ChannelHandler다
 * 이 객체는 SocketChannel을 초기화하는 역할을 한다 SocketChannel이 초기화 되면 ChannelPipeline에서 ChannelIntializer가 제거
 * 서버와 비슷하게 채널 파이프라인 생성
 *
 * 각각의 SocketChannel은 ChannelPipeline을 가지고 있다 채널파이프라인은 ChannelHandler
 * 인스턴스의 리스트다 EventLoop이 데이터를 SocketChannel에서 읽으면 데이터는 파이프라인에 있는 첫번째 채널핸들러에게 넘겨진다
 * 첫 번째 핸들러는 넘겨받은 데이터를 처리하고 필요한 경우 파이프라인의 다음 핸들러로 데이터를 넘길 수 있다
 * 데이터를 SocketChannel로 쓰는 경우도 마찬가지로 ChannelPipeline을 타게 되며 핸들러들은 거친다음 SocketChannel로 쓰여지게됩니다
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {


    /**
     * 채널 파이프라인은 Netty 애플리케이션의 핵심이다
     * 각 TCP 연결에 해당하는 SocketChannel은 ChannelPipeline을 가지고 있다
     * 채널 파이프라인은 ChannelHandler 인스턴스의 리스트다 각 ChannelHandler 인스턴스들은 SocketChannel 쪽으로 데이터를 넘기거나 SocketChannel 쪽에서 데이터를 얻어온다.
     * @param ch
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new LineBasedFrameDecoder(65536));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new ClientHandler());
    }
}