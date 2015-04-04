import java.io.File;
import java.util.List;

import javax.net.ssl.SSLException;

import servers.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.*;
import java.util.concurrent.*;


public final class MainClient extends Thread{

	static final boolean SSL = System.getProperty("ssl") != null;
	static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

	static ConcurrentLinkedQueue<String> stack;



	public static void main(String[] args) throws Exception {


		Server server = new Server();
		File confFile = new File(args[0]);

		ServerConf servconf = server.parseConf(confFile);
		
		ArrayList<AdjacentNode> adjList = servconf.getAdjacentNodes();
		
		int port = adjList.get(0).getPort();
		
		String host = adjList.get(0).getHost();
		
		System.out.println(String.valueOf(port)+" "+ host);
		
		
		
//		stack = new ConcurrentLinkedQueue<String>();


		final SslContext sslCtx;
		if (SSL) {
			sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
		} else {
			sslCtx = null;
		}
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new ClientInitializer(sslCtx));

			// Make a new connection.
			Channel ch = b.connect(host, port).sync().channel();

			// Get the handler instance to initiate the request.
			EchoClientHandler handler = ch.pipeline().get(EchoClientHandler.class);

			// Request and get the response.

			handler.sendHi();

			InBoundWorker inboundworker = new MainClient.InBoundWorker();
			//Thread thread=new Thread(inboundworker);
			//thread.start();

			//  ch.close();


		} finally {
			group.shutdownGracefully();
		}
	}


	public static final class InBoundWorker implements Runnable
	{
		
		public void run()
		{
			String s;
			while(true)
			{
				if(stack.isEmpty())
				{
					continue;
				}
				else
				{
					s= stack.poll();
					System.out.println("Server Sent" + s);
				}
			}
		}

	}

}

