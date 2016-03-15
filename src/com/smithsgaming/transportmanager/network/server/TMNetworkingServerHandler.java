

package com.smithsgaming.transportmanager.network.server;

import com.smithsgaming.transportmanager.network.message.*;
import com.smithsgaming.transportmanager.util.*;
import io.netty.buffer.*;
import io.netty.channel.*;

/**
 * Created by marcf on 3/14/2016.
 */
public class TMNetworkingServerHandler extends SimpleChannelInboundHandler<TMNetworkingMessage> {
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, TMNetworkingMessage tmNetworkingMessage) throws Exception {
        System.out.println("Attempting to handle NBTPayLoad...");
        TMNetworkingMessage returnMessage = tmNetworkingMessage.onReceived(channelHandlerContext.channel(), Side.SERVER);

        if (returnMessage != null) {
            channelHandlerContext.write(returnMessage);
        }
    }

    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
