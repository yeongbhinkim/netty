package org.example.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.rbpClient.service.RbpClientService;

/**
 * 서버로 문자열을 던지면 서버는 문자를 좀 더 붙여서 클라이언트로 던져준다.
 * 클라이언트는 서버로부터 문자열을 받아 channelRead0() 메소드를 호출한다.
 * 이 메소드는 결국 받은 문자열을 화면에 출력한다.
 *
 * 각각의 SocketChannel은 ChannelPipeline을 가지고 있다 채널파이프라인은 ChannelHandler
 * 인스턴스의 리스트다 EventLoop이 데이터를 SocketChannel에서 읽으면 데이터는 파이프라인에 있는 첫번째 채널핸들러에게 넘겨진다
 * 첫 번째 핸들러는 넘겨받은 데이터를 처리하고 필요한 경우 파이프라인의 다음 핸들러로 데이터를 넘길 수 있다
 * 데이터를 SocketChannel로 쓰는 경우도 마찬가지로 ChannelPipeline을 타게 되며 핸들러들은 거친다음 SocketChannel로 쓰여지게됩니다
 */
public class ClientHandler extends SimpleChannelInboundHandler {

    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        System.out.println((String)msg);
        System.out.println("==============2===============");
        String message;
        message = (String)msg;
        System.out.println(message);


    }

    
    //예외가 발생시 호출
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}