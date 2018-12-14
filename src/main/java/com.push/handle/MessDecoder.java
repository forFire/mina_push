package com.push.handle;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *解码
 *@author zf 
 */
public class MessDecoder implements MessageDecoder {
	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * byte字节型是JAVA中最小的数据类型，它在内存中占8位（8个bit），取值范围从-128到127
	 * 将byte[]转化十六进制的字符串,注意这里b[ i ] & 0xFF将一个byte和
	 * 0xFF进行了与运算,然后使用Integer.toHexString取得了十六进制字符串,可以看出 b[ i ] &
	 * 0xFF运算后得出的仍然是个int
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}

		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		//解析服务端传回来的数据
//		System.out.println("bytesToHexString=============>"+bytesToHexString(in.array()));
		//交通部返回数据就是登录成功。。。。。不应该解析么？？？？？
		if(bytesToHexString(in.array()) !=""){
			log.info("---------解析服务端传回来的数据 MessageDecoderResult.OK -----------", this.getClass().getName());
			return MessageDecoderResult.OK;//读取数据判断当前数据包是否可进行decode，返回MessageDecoderResult.OK
			
		}else{
			log.info("---------解析服务端传回来的数据 MessageDecoderResult.NEED_DATA -----------", this.getClass().getName());
			return MessageDecoderResult.NEED_DATA;
		}
	}

	public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
				try {
					in.position(in.limit());//在return之前加上in.position(in.limit());把指针复位.
					// 调用推送数据接口
//					session.write(tm.carRegisterMes());
					return MessageDecoderResult.OK;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return MessageDecoderResult.OK;
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
	}



}
