


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import generated.App.Request;
import generated.App.*;
import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandlerContext;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
24   * Handler implementation for the echo client.  It initiates the ping-pong
25   * traffic between the echo client and server by sending the first message to
26   * the server.
27   */
public class EchoClientHandler extends SimpleChannelInboundHandler<Request> {

//	private final ByteBuf firstMessage;

	private volatile Channel channel;
    private final BlockingQueue<Request> answer = new LinkedBlockingQueue<Request>();


    public EchoClientHandler() {
        super(false);
    }
    
    
    public void sendHi() {

    	Request serverresponse;
    	Request.Builder f = Request.newBuilder();
    	f.setMsg("Hi");
    	channel.writeAndFlush(f.build());
    	for (;;) {
            try {
                serverresponse = answer.take();
                System.out.println(serverresponse.getMsg());
                break;
            } catch (InterruptedException ignore) {
              System.out.println(ignore.toString());
            }
        }

	}
	

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        channel = ctx.channel();
    }

    public void messageReceived(ChannelHandlerContext ctx, Request times) {
    	System.out.println(times.getMsg());
    	MainClient.stack.add(times.getMsg());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        System.out.println(cause.toString());
        ctx.close();
    }


	@Override
	protected void channelRead0(ChannelHandlerContext arg0, Request arg1)
			throws Exception {
		System.out.println(arg1.getMsg());
		
		answer.add(arg1);

		
		
		
		
	}
}