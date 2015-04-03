/* Unless required by applicable law or agreed to in writing, software
   * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
   * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
   * under the License.
   */
  //package io.netty.example.echo;
  
  import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import generated.App.Request;
import generated.App.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

  
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
	    	System.out.println("Channel Initialized");
	    	//System.out.println(ctx.name());
	        channel = ctx.channel();
	    }
	    
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Request arg1)
				throws Exception {
			 System.out.println(arg1.getMsg());
			 int count=0;
	while(true)
			 {
			 Request.Builder f = Request.newBuilder();
			 f.setMsg("Hi From Server "+ String.valueOf((count++)));
			// ctx.writeAndFlush(f.build());
			channel.writeAndFlush(f.build());
			 }
			
		}
		
		
   
  }