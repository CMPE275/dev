/* Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
//package io.netty.example.echo;


//import ServerHandler.ConnectionClosedListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import generated.App.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import queue.DiscreteQueue;
import queue.ChannelQueue;
import queue.QueueFactory;


/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends SimpleChannelInboundHandler<Request> {

	public volatile Channel channel;

	public EchoServerHandler()
	{
		super(false);
	}

	public void messageReceived(ChannelHandlerContext ctx, Request request) {

		System.out.println(request.getMsg());

	}

	public void channelRegistered(ChannelHandlerContext ctx) {
		
		channel = ctx.channel();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Request arg1)
			throws Exception {
	//	System.out.println(arg1.getMsg());
	
		QueueFactory.getInstance().enqueueRequest(arg1, ctx.channel());

	
	}

	public static class ConnectionClosedListener implements ChannelFutureListener {

		private ChannelQueue sq;

		public ConnectionClosedListener(ChannelQueue sq) {
			this.sq = sq;
		}


		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			// Note re-connecting to clients cannot be initiated by the server
			// therefore, the server should remove all pending (queued) tasks. A
			// more optimistic approach would be to suspend these tasks and move
			// them into a separate bucket for possible client re-connection
			// otherwise discard after some set period. This is a weakness of a
			// connection-required communication design.

			if (sq != null)
				sq.shutdown(true);
			sq = null;
		}

	}



}