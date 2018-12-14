package com.push.handle;

import com.alibaba.fastjson.JSON;
import com.push.model.Device;
import com.push.model.Position;
import com.push.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class DeviceCache {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private static final String DEVICE = "device";

	private static final String CARCENTAL = "allCars";
	
	//获取所有有效车辆信息
	public List<Device> getCars(){

//		setCars();

		List<Object> lists = redisTemplate.opsForHash().values(CARCENTAL);
		List<Device> listDevice = new ArrayList<Device>();
		for (Object obj : lists) {
//			JsonParser parse =new JsonParser();  //创建json解析器
//			JsonObject  device2 = (JsonObject )parse.parse(obj.toString());
			Device device = JsonUtils.str2Obj(obj.toString(), Device.class);
//			device.setVehicleNo(device.get("vehicleNo").toString());
			listDevice.add(device);
		}
		return listDevice;
	}


	public Device getDevice(String deviceSn) {
		String json = (String) redisTemplate.opsForHash().get(CARCENTAL, deviceSn);
		if (json != null && !"".equals(json)) {
			return JsonUtils.str2Obj(json, Device.class);
		}
		return null;
	}


	public  void setCars(){
		List<Device> listDevice = new ArrayList<>();
		Device device = new Device();
		device.setVehicleNo("京 QY09H9");
		device.setSn("sn001");
		listDevice.add(device);
		Device device1 = new Device();
		device1.setVehicleNo(" 京 QU9U69");
		device1.setSn("sn001");
		listDevice.add(device1);
		Device device2 = new Device();
		device2.setVehicleNo(" 京 QY10H1");
		device2.setSn("sn002");
		listDevice.add(device2);
		Device device3 = new Device();
		device3.setVehicleNo(" 京 QY10H2");
		device3.setSn("sn003");
		listDevice.add(device3);
		Device device4 = new Device();
		device4.setVehicleNo(" 京 QY10H7");
		device4.setSn("sn004");
		listDevice.add(device4);
		redisTemplate.opsForHash().put("allCars","sn000",JSON.toJSON(device).toString());
		redisTemplate.opsForHash().put("allCars","sn001",JSON.toJSON(device1).toString());
		redisTemplate.opsForHash().put("allCars","sn002",JSON.toJSON(device2).toString());
		redisTemplate.opsForHash().put("allCars","sn003",JSON.toJSON(device3).toString());
		redisTemplate.opsForHash().put("allCars","sn004",JSON.toJSON(device4).toString());
//		redisTemplate.opsForHash().put(CARCENTAL,"XXXX",JSON.toJSON(listDevice).toString());
//		redisTemplate.opsForHash().put("position","XXXX",JSON.toJSON(listDevice).toString());

		Position position = new Position();
		position.setDeviceSn("sn004");
		position.setLat(39.9798650);
		position.setLng(116.4985916018D);
		position.setMode("A");
		position.setSpeed(11.1);
		position.setDirection(12.12F);

		redisTemplate.opsForHash().put("position", "sn004",JsonUtils.obj2Str(position));

//		setPosition("sn004",position);

	}
	public void setPosition(String deviceSn, Position position) {
		String json = JsonUtils.obj2Str(position);
		redisTemplate.opsForHash().put("position", deviceSn, json);
	}

}
