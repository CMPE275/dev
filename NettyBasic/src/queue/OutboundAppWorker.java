/*
 * copyright 2015, gash
 * 
 * Gash licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import queue.DiscreteQueue.OneQueueEntry;
import generated.App.Request;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import com.google.protobuf.GeneratedMessage;

public class OutboundAppWorker extends Thread {
	protected static Logger logger = LoggerFactory.getLogger("server");

	int workerId;
	DiscreteQueue sq;
	boolean forever = true;

	public OutboundAppWorker(ThreadGroup tgrp, int workerId, DiscreteQueue sq) {
		super(tgrp, "outbound-" + workerId);
		this.workerId = workerId;
		this.sq = sq;

		if (sq.getOutbound() == null)
			throw new RuntimeException("connection worker detected no outbound queue");
	}

	@Override
	public void run() {
		//Channel conn = sq.channel;
		//		if (conn == null || !conn.isOpen()) {
		//			PerChannelQueue.logger.error("connection missing, no outbound communication");
		//			return;
		//		}
		Request request;
		Channel conn;

		while (true) {
			if (!forever && sq.getOutbound().size() == 0)
				break;

			try {
				// block until a message is enqueued
				OneQueueEntry oneQueueEntry = sq.getOutbound().take();
				request = oneQueueEntry.getReq();
				conn = oneQueueEntry.getChannel();

				if (conn.isWritable()) {
					boolean rtn = false;
					if (conn != null && conn.isOpen() && conn.isWritable()) {
						ChannelFuture cf = conn.write(request);

						// blocks on write - use listener to be async
						cf.awaitUninterruptibly();
						rtn = cf.isSuccess();
						if (!rtn){
							sq.getOutbound().putFirst(oneQueueEntry);
						}

					}
				}else
					sq.getOutbound().putFirst(oneQueueEntry);
			} catch (InterruptedException ie) {
				break;
			} catch (Exception e) {
				logger.error("Unexpected communcation failure", e);
				break;
			}
		}

		if (!forever) {
			logger.info("connection queue closing");
		}
	}
}
