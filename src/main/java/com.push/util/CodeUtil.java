package com.push.util;

public class CodeUtil {
	
	/**
	 * 补齐长度
	 * @param value
	 * @param size
	 * @return
	 */
	public static String toDeviceString(String value, int size){		
		while (value.length() < size) {
			value = "!" + value;
		}
		return value;
	}
	
	
	/**
	 * 转换十进制
	 * @param hexString
	 * @return
	 */
	public static int toInt(String hexString){
		
		return Integer.parseInt(hexString, 16);
	}

	/**
	 * 转换十进制
	 * @param hexString
	 * @return
	 */
	public static long toLong(String hexString){
		
		return Long.parseLong(hexString, 16);
	}
	
	/**
	 * 转换十六进制
	 * @param i
	 * @param size
	 * @return
	 */
	public static String toHexString(long i, int size) {

		
		String value = Long.toHexString(i);

		while (value.length() < size) {
			value = "0" + value;
		}

		return value;
	}

	//0x7e<--0x7d02，0x7d<--0x7d01，转意还原
	public static byte[] tranToOriginal(int start,byte[] newBytes)
	{
		char a1=0x7d;
		char a2=0x02;
		char b1=0x7d;
		char b2=0x01;
		for(int i=start;i<newBytes.length-1;i++)
		{
			if(newBytes[i]==a1&&newBytes[i+1]==a2)
			{
				newBytes[i]=0x7e;
				newBytes=forwardByte( newBytes, i+1);
			}
			if(newBytes[i]==b1&&newBytes[i+1]==b2)
			{
				newBytes[i]=0x7d;
				newBytes=forwardByte( newBytes, i+1);
			}
		}
		return newBytes;
				
	}
	//删除 needBytes 第i位，并把后面的byte前置 i从0开始
	public static byte[] forwardByte(byte[] needBytes,int deloffset)
	{
		byte[] newBytes=new byte[needBytes.length-1];
		for(int i=0;i<needBytes.length-1;i++)
		{
			if(i<deloffset)
				newBytes[i]=needBytes[i];
			if(i>deloffset)
				newBytes[i-1]=needBytes[i];
		}
		return newBytes;
	}
		
	/**
	 * 十六进制字符串转化为byte数组
	 * @return the array of byte
	 */
	public static final byte[] hex2byte(String hex) throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
//			System.out.println("16进制="+swap+"-----10进制="+byteint);
//			System.out.println(j+"="+new Integer(byteint).byteValue());
			b[j] = new Integer(byteint).byteValue();
		}
//		System.out.println(b);
		return b;
	}

	/**
	 * @param args len 长度,
	 * 			   checkString 校验数组
	 */
    
    public static  int CRC16Caculate(int len,byte[] checkString)
    {
    	 int CRC;
    	   int i;
    	   short CRC_H4=0;
    	   int []  CRCTable =
    	           {0x0000,0x1021,0x2042,0x3063,0x4084,0x50A5,0x60C6,0x70E7,0x8108,
    	   	                    0x9129,0xA14A,0xB16B,0xC18C,0xD1AD,0xE1CE,0xF1EF };
    	   CRC= 0xFFFF;
    	   int position=0;
    	   for (i = 0; i <len; i++)
    	        {
    	            CRC_H4 = (short)(CRC >>12);
    	            CRC = ((CRC<< 4)%65536);
    	           
    	            position=CRC_H4^(checkString[i]>>4);
    	            if(position<0)
    	            {
    	            	position+=16;
    	            }
    	            CRC = CRC^CRCTable[position];
    	            CRC_H4 = (short)(CRC>>12);
    	            CRC = ((CRC<< 4)%65536);
    	            position=CRC_H4^(checkString[i]&0x0F);
    	            if(position<0)
    	            {
    	            	position+=16;
    	            }
    	            CRC = CRC^CRCTable[position];
    	        }
    	    return CRC;
    }
    
    /**
	 * Convert byte value to string.
	 *
	 * @param value Byte value.
	 * @return
	 *     <p>Hex string of byte value.</p>
	 */
	public static String toHexString(byte value)
	{
		//String.
		String string = "";
		//Convert to hex string.
		for(int i = 0;i < 2;i ++)
		{
			//Append char.
			string += Character.forDigit((int)((value >> (4 * (2 - i - 1))) & 0x0f),16);
		}
		//Return string.
		return string;
	}
    
	/**
	 * Convert byte array to string.
	 *
	 * @param value Byte array.
	 * @return
	 *     <p>Hex string of byte array value.</p>
	 */
    public static String toHexString(byte[] bytes)
	{
		//String.
		String hexes = "";
		//Convert to hex string.
		for(int i = 0;bytes != null && i < bytes.length;i ++)
		{
			//Append char.
			hexes += toHexString(bytes[i]);
		}
		//Return string.
		return hexes;
	}
    
	/**
	 * Convert byte array to Int.
	 *
	 * @param value Byte array.
	 * @return
	 *     <p>Hex string of byte array value.</p>
	 */
    public static int toHexInt(byte[] bytes)
	{

		//String.
		String hexes = "";
		//Convert to hex string.
		for(int i = 0;bytes != null && i < bytes.length;i ++)
		{
			//Append char.
			hexes += toHexString(bytes[i]);
		}
		//Return string.
		return toInt(hexes);
	}
    
    /**
	 * Convert byte array to Int.
	 *
	 * @param value Byte array.
	 * @return
	 *     <p>Hex string of byte array value.</p>
	 */
    public static Long toHexLong(byte[] bytes)
	{
		//String.
		String hexes = "";
		//Convert to hex string.
		for(int i = 0;bytes != null && i < bytes.length;i ++)
		{
			//Append char.
			hexes += toHexString(bytes[i]);
		}
		//Return string.
		return toLong(hexes);
	}
	/**
	 * Convert byte array to string.
	 *
	 * @param value Byte array.
	 * @return
	 *     <p>Hex string of byte array value.</p>
	 */
    
    public static byte[] negativeToPositive(byte[] thebyte)
	{
		//String.
		String hexes = "";
		
		byte[] bytes=thebyte;
		
		 for (int i=0;i<bytes.length;i++){
	            int b=0;
	            if(bytes[i]>=0)
	            	continue;
	            for (int j=0;j<8;j++){
	                int bit = (bytes[i]>>j&1)==0?1:0;
	                b += (1<<j)*bit;
	            }
	            bytes[i]=(byte)(b+1);
	        }
		 
		//Return string.
		return bytes;
	}
    /*
     * Bit0:=1运营，=0非运营
		Bit1:=1上行，=0下行
		Bit2:=1进入，=0离开

     */
    public static int[] analysisUpDown(byte runstat)
    {
    	int[] stat=new int[3];
    	//=1运营，=0非运营
    	if((runstat & 0x01)==0x01)
    	{
    		stat[0]=1;
    	}
    	//1上行，=0下行
    	if((runstat & 0x02)==0x02)
    	{
    		stat[1]=1;
    	}
    	//1进入，=0离开
    	if((runstat & 0x04)==0x04)
    	{
    		stat[2]=1;
    	}
    	return stat;
    }
    
}
