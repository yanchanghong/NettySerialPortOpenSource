package changhongyun.NettySerialPortOpenSource.serialPort.netty;

import java.io.IOException;

public interface ApPortConnection {
	/**
	 * send message to ap port
	 * @param data
	 */
	public void sendMessage(byte[] data) throws IOException ;
	
	/**
	 * read message from ap port
	 * @param data
	 */
	public void readMessage(byte[] data);
	
	/**
	 * close the connection
	 * @throws Exception
	 */
	public void close() throws Exception;
}
