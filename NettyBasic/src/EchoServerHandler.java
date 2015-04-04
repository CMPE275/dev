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
	
		QueueFactory.getInstance().enqueueIn(arg1, ctx.channel());

	
	}

	public static class ConnectionClosedListener implements ChannelFutureListener {

		private ChannelQueue sq;

		public ConnectionClosedListener(ChannelQueue sq) {
			this.sq = sq;
		}


		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			if (sq != null)
				sq.shutdown(true);
			sq = null;
		}

	}



}