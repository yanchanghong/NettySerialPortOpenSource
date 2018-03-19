package changhongyun.NettySerialPortOpenSource.serialPort.netty;


import org.apache.log4j.Logger;

import changhongyun.NettySerialPortOpenSource.util.Convert;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SerialDebugIncoming extends  ChannelInboundHandlerAdapter{
	private static final Logger logger = Logger.getLogger(SerialDebugIncoming.class.getName());
	    @Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		 	ByteBuf  buf = (ByteBuf)msg;
		 	byte[] array = new byte[buf.readableBytes()];
			buf.getBytes(0, array);
			logger.info("receiving data:"+ Convert.ByteArrayToHexString(array));
			
			ctx.fireChannelRead(msg);
		}
}
