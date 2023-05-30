package org.example.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * TCP 연결이 accept 되었을 때 실행된다
 * 채널에 이벤트가 발생하면 소켓 채널에서 읽어 들이는 것인지 소켓 채널로 쓰는 것인지에 따라서 파이프 라인의 핸들러가 수행된다.
 */
public class EchoServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //LineBasedFrameDecoder() 통해 네트워크에서 전송되는 바이트 값을 읽어 라인문자열로 만들어주고
        pipeline.addLast(new LineBasedFrameDecoder(65536));
        //필요하다면 디코딩을 한다음
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        //EchoServerHandler() 호출
        pipeline.addLast(new EchoServerHandler());
        
        //이후 write()가 되면 StringEncoder()을 통해 네트워크 너머로 데이터 전송
    }
}