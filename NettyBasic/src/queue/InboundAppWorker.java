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

import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import queue.DiscreteQueue.OneQueueEntry;
//import poke.comm.App.PokeStatus;
import generated.App.Request;
//import poke.server.resources.Resource;
//import poke.server.resources.ResourceFactory;
//import poke.server.resources.ResourceUtil;
import Management.*;



import Management.HeartbeatManager;

import com.google.protobuf.GeneratedMessage;

public class InboundAppWorker extends Thread {
	protected static Logger logger = LoggerFactory.getLogger("server");

	int workerId;
	DiscreteQueue sq;
	boolean forever = true;

	public InboundAppWorker(ThreadGroup tgrp, int workerId, DiscreteQueue sq) {
		super(tgrp, "inbound-" + workerId);
		this.workerId = workerId;
		this.sq = sq;

		if (sq.getInbound() == null)
			throw new RuntimeException("connection worker detected null inbound queue");
	}

	@Override
	public void run() {
		System.out.println("Inbound App worker started");


		while (true) {
			if (!forever && sq.getInbound().size() == 0)
				break;

			try {
				// block until a message is enqueued
				OneQueueEntry oneQueueEntry = sq.getInbound().take();
				Channel conn = oneQueueEntry.getChannel();
				
				Request request = oneQueueEntry.getReq();

				// process request and enqueue response
				if (oneQueueEntry.getReq() instanceof Request) {
					Request req = ((Request) oneQueueEntry.getReq());

				
				//Resource rsc = ResourceFactory.getInstance().resourceInstance(req.getHeader());

					Request reply = null;
//					if (rsc == null) {
//						logger.error("failed to obtain resource for " + req);
//						reply = ResourceUtil
//								.buildError(req.getHeader(), PokeStatus.NORESOURCE, "Request not processed");
//					} else {
//						// message communication can be two-way or one-way.
//						// One-way communication will not produce a response
//						// (reply).
//					
//					}
					 HeartbeatManager heartbeatManager = HeartbeatManager.getInstance();
					 
					 heartbeatManager.processRequest(req,conn);
				}

			} catch (InterruptedException ie) {
				break;
			} catch (Exception e) {
				logger.error("Unexpected processing failure", e);
				break;
			}
		}

		if (!forever) {
			logger.info("connection queue closing");
		}
	}
}