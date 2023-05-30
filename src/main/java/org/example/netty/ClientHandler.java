package org.example.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 서버로 문자열을 던지면 서버는 문자를 좀 더 붙여서 클라이언트로 던져준다.
 * 클라이언트는 서버로부터 문자열을 받아 channelRead0() 메소드를 호출한다.
 * 이 메소드는 결국 받은 문자열을 화면에 출력한다.
 */
public class ClientHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        System.out.println((String)msg);
    }

    
    //예외가 발생시 호출
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}