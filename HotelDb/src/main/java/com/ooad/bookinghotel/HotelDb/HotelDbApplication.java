package com.ooad.bookinghotel.HotelDb;

import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ooad.bookinghotel.Controller"} )
public class HotelDbApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HotelDbApplication.class, args);
		log.info("hi, i am main.");
	}

	@Autowired
	private Environment environment;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				String origins = environment.getProperty("cors.urls");
				log.info("cors whitelist:"+origins);

				registry.addMapping("/**").allowedOrigins(origins).allowedMethods("*");
			}
		};
	}

	@Autowired // This means to get the bean called systemConfigRepository
	private SystemConfigRepository systemConfigRepository;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private HotelRoomRepository hotelRoomRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... strings) throws Exception {

		UpdateSchema();

//		UpdateHotelList();
		UpdateHotelListBySqlScript();
	}

	private void UpdateHotelListBySqlScript(){
		log.info("Check Hotel List Update Time.");

		List<SystemConfig> hotelListUpdateTimeList = systemConfigRepository.findByName(SystemConstant.ConfigHotelListUpdateTime);
		SystemConfig hotelUpdateTime = null;
		boolean updateData = true;

		try {
			File resource = new ClassPathResource("systemFile/HotelList.json").getFile();
			String HotelListString = new String(Files.readAllBytes(resource.toPath()));

			JSONParser jsonParser = new JSONParser(HotelListString);
			Map<String, Object> obj = jsonParser.parseObject();
			String fileDate =  obj.get("UpdateTime").toString();

			if(hotelListUpdateTimeList.size()>0){
				hotelUpdateTime = hotelListUpdateTimeList.get(0);
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date lastDbUpdate = formatter1.parse(hotelUpdateTime.getValue());
				Date fileUpdateTime = formatter1.parse(fileDate);
				updateData = fileUpdateTime.compareTo(lastDbUpdate) > 0;
			}

			if(updateData){
				log.info("Prepare update.");

				ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>> ) obj.get("List");
				ArrayList<Object[]> HotelRooms = new ArrayList<Object[]>() {};

				List<Object[]> Hotels = list.stream().map(hotel -> {
					Integer hotelId =  Integer.parseInt(hotel.get("HotelID").toString());
					Integer hotelStar =  Integer.parseInt(hotel.get("HotelStar").toString());
					String locality = hotel.get("Locality").toString();
					String address = hotel.get("Street-Address").toString();
					Object[] newHotel = new Object[]{
							address,
							hotelId,
							locality,
							locality + hotelId,
							hotelStar
					};

					ArrayList<Map<String, Object>>  rooms = (ArrayList<Map<String, Object>> )hotel.get("Rooms");
					for(Map<String, Object> room : rooms){
						String roomType = room.get("RoomType").toString();
						RoomType rtCode = RoomType.valueOf(roomType);
						Integer price = Integer.parseInt( room.get("RoomPrice").toString());
						Integer quantity = Integer.parseInt( room.get("Number").toString());
						HotelRooms.add(new Object[]{
								hotelId,
								price,
								quantity,
								rtCode.number
						});
					}

					return newHotel;
				}).collect(Collectors.toList());

				log.info("room size: "+HotelRooms.size());
				log.info("hotel size: "+Hotels.size());
				log.info(fileDate);

				//如果檔案重複，就放棄新增
				jdbcTemplate.batchUpdate("INSERT INTO hotel (create_time, update_time, address, json_file_id, locality, name, star) " +
						"VALUES (current_timestamp, current_timestamp, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE id=id ;", Hotels);

				//如果檔案重複，就放棄新增
				jdbcTemplate.batchUpdate("INSERT INTO hotel_room (hotel_id, price, quantity, room_type, create_time, update_time) " +
						"VALUES (?, ?, ?, ?, current_timestamp, current_timestamp) ON DUPLICATE KEY UPDATE id=id ;", HotelRooms);

				log.info("DONE.");
			}

			if(hotelUpdateTime == null){
				hotelUpdateTime = new SystemConfig();
				hotelUpdateTime.setName(SystemConstant.ConfigHotelListUpdateTime);
				hotelUpdateTime.setExplanation("上次旅館更新時間");
			}

			hotelUpdateTime.setValue(fileDate);
			systemConfigRepository.save(hotelUpdateTime);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void UpdateSchema(){
		log.info("Update Schema.");

		String table_name_ints = "ints";
		List<Map<String, Object>> check_ints = jdbcTemplate.queryForList("show tables like '"+ table_name_ints +"';");
		System.out.println("ints exist: " + check_ints);
		if(check_ints==null || check_ints.size()==0){
			jdbcTemplate.execute("CREATE TABLE ints ( i tinyint );");
			jdbcTemplate.execute("INSERT INTO ints VALUES (0),(1),(2),(3),(4),(5),(6),(7),(8),(9); ");
		}

		String table_name_calendar_table = "calendar_table";
		List<Map<String, Object>> check_calendar_table = jdbcTemplate.queryForList("show tables like '"+ table_name_calendar_table +"';");
		System.out.println("calendar_table exist: " + check_calendar_table);
		if(check_calendar_table==null || check_calendar_table.size()==0){
			jdbcTemplate.execute("CREATE TABLE calendar_table ( " +
					"    dt DATE NOT NULL PRIMARY KEY, " +
					"    y SMALLINT NULL, " +
					"    q tinyint NULL, " +
					"    m tinyint NULL, " +
					"    d tinyint NULL, " +
					"    dw tinyint NULL, " +
					"    monthName VARCHAR(9) NULL, " +
					"    dayName VARCHAR(9) NULL, " +
					"    w tinyint NULL, " +
					"    isWeekday BINARY(1) NULL, " +
					"    isHoliday BINARY(1) NULL, " +
					"    holidayDescr VARCHAR(32) NULL, " +
					"    isPayday BINARY(1) NULL " +
					");");
			jdbcTemplate.execute("INSERT INTO calendar_table (dt) " +
					"SELECT DATE('2019-12-01') + INTERVAL a.i*10000 + b.i*1000 + c.i*100 + d.i*10 + e.i DAY " +
					"FROM ints a JOIN ints b JOIN ints c JOIN ints d JOIN ints e " +
					"WHERE (a.i*10000 + b.i*1000 + c.i*100 + d.i*10 + e.i) <= 11322 " +
					"ORDER BY 1;");
		}

		String view_name_hotel_info = "hotel_info";
		List<Map<String, Object>> check_view_name_hotel_info = jdbcTemplate.queryForList("show tables like '"+ view_name_hotel_info +"';");
		System.out.println("hotel_info exist: " + check_view_name_hotel_info);
		if(check_view_name_hotel_info==null || check_view_name_hotel_info.size()==0){
			jdbcTemplate.execute(" CREATE OR REPLACE VIEW hotel_info  AS " +
					" select " +
					" hotel.id, hotel.star, hotel.locality, hotel.address, hotel.json_file_id, hotel.name, hotel_room.room_type, hotel_room.price,  hotel_room.quantity, calendar_table.dt " +
					" from hotel " +
					" inner join hotel_room on hotel_id = hotel.json_File_id " +
					" inner join calendar_table on 1=1 " +
					" where hotel_room.quantity > 0;");
		}

		String view_name_booked_room = "booked_room";
		List<Map<String, Object>> check_view_name_booked_room = jdbcTemplate.queryForList("show tables like '"+ view_name_booked_room +"';");;
		System.out.println("booked_room exist: " + check_view_name_booked_room);
		if(check_view_name_booked_room==null || check_view_name_booked_room.size()==0){
			jdbcTemplate.execute("CREATE OR REPLACE VIEW booked_room  AS " +
					"select calendar_table.dt, booked.hotel_id, booked.room_type, count(booked.bookingId)  as quantity " +
					"from calendar_table " +
					"left join ( " +
					" select  " +
					" booking.id as bookingId, booking.hotel_id, booking.hotel_room_id, ordering.start_date, ordering.end_date, booking.is_disabled, " +
					" hotel_room.room_type " +
					" from ordering  " +
					"inner join booking on ordering.id = booking.order_id and booking.is_disabled <> true and ordering.is_disabled <> true " +
					"inner join hotel_room on hotel_room.id = booking.hotel_room_id) AS booked " +
					"on DATE(booked.start_date) <= calendar_table.dt and DATE(booked.end_date) >= calendar_table.dt " +
					"group by calendar_table.dt, booked.hotel_id, booked.room_type; ");
		}

		String view_name_booked_hotel_info = "booked_hotel_info";
		List<Map<String, Object>> check_view_name_booked_hotel_info = jdbcTemplate.queryForList("show tables like '"+ view_name_booked_hotel_info +"';");
		System.out.println("booked_hotel_info exist: " + check_view_name_booked_hotel_info);
		if(check_view_name_booked_hotel_info==null || check_view_name_booked_hotel_info.size()==0){
			jdbcTemplate.execute("CREATE OR REPLACE VIEW  booked_hotel_info  AS " +
					" select hotel_info.dt, hotel_info.id, hotel_info.star, hotel_info.locality, hotel_info.address, hotel_info.json_file_id, " +
					" hotel_info.name, hotel_info.room_type, hotel_info.quantity, hotel_info.price, booked_room.quantity as booked_quantity " +
					" from hotel_info " +
					" left join booked_room " +
					" on booked_room.dt = hotel_info.dt and booked_room.room_type = hotel_info.room_type " +
					" and booked_room.hotel_id = hotel_info.json_file_id");
		}

		String view_name_order_info = "order_info";
		List<Map<String, Object>> check_view_name_order_info = jdbcTemplate.queryForList("show tables like '"+ view_name_order_info +"';");
		System.out.println("order_info exist: " + check_view_name_order_info);
		if(check_view_name_order_info==null || check_view_name_order_info.size()==0){
			jdbcTemplate.execute("select hotel_room_id, ordering.id, ordering.create_time, ordering.update_time, " +
					"ordering.discount, ordering.memo, ordering.total, ordering.user_id, " +
					"ordering.is_disabled, ordering.end_date, ordering.start_date, ordering.is_paid, hotel.json_file_id, " +
					"hotel.star, hotel.locality, hotel.address, hotel.name, booking_info.room_type, booking_info.price, " +
					"booking_info.is_disabled as booked_is_disabled, count(ordering.id) as booked_quantity " +
					"from ordering " +
					"inner join ( " +
					" select booking.*, hotel_room.room_type, hotel_room.price " +
					" from booking " +
					" inner join hotel_room on booking.hotel_room_id = hotel_room.id " +
					") AS booking_info on booking_info.order_id = ordering.id " +
					"inner join hotel on booking_info.hotel_id = hotel.json_file_id " +
					"group by hotel_room_id, ordering.id, ordering.create_time, ordering.update_time, ordering.discount, ordering.memo, " +
					"ordering.total, ordering.user_id, " +
					"ordering.is_disabled, ordering.end_date, ordering.start_date, ordering.is_paid, hotel.json_file_id, " +
					"hotel.star, hotel.locality, hotel.address, hotel.name, booking_info.room_type, " +
					"booking_info.price,booking_info.is_disabled ");
		}

		List<Map<String, Object>> check_hotel_unique_index = jdbcTemplate.queryForList("show keys from hotel where key_name='hotel_unique_index'");
		if(check_hotel_unique_index==null || check_hotel_unique_index.size()==0){
			jdbcTemplate.execute("ALTER TABLE hotel ADD CONSTRAINT hotel_unique_index UNIQUE (json_File_id);");
		}

		List<Map<String, Object>> check_hotel_room_unique_index = jdbcTemplate.queryForList("show keys from hotel_room where key_name='hotel_room_unique_index'");
		if(check_hotel_room_unique_index==null || check_hotel_room_unique_index.size()==0){
			jdbcTemplate.execute("ALTER TABLE hotel_room ADD CONSTRAINT hotel_room_unique_index UNIQUE (hotel_id, room_type);");
		}

		jdbcTemplate.execute("ALTER TABLE hotel MODIFY id INT NOT NULL AUTO_INCREMENT;");
		jdbcTemplate.execute("ALTER TABLE hotel_room MODIFY id INT NOT NULL AUTO_INCREMENT;");
		jdbcTemplate.execute("ALTER TABLE system_config MODIFY id INT NOT NULL AUTO_INCREMENT;");
		jdbcTemplate.execute("ALTER TABLE booking MODIFY id INT NOT NULL AUTO_INCREMENT;");
		jdbcTemplate.execute("ALTER TABLE ordering MODIFY id INT NOT NULL AUTO_INCREMENT;");
	}

	// 太慢了
	private void UpdateHotelList(){
		log.info("Check Hotel List Update Time.");

		List<SystemConfig> hotelListUpdateTimeList = systemConfigRepository.findByName(SystemConstant.ConfigHotelListUpdateTime);
		SystemConfig hotelUpdateTime = null;
		boolean updateData = true;

		try {

			ClassLoader classLoader = getClass().getClassLoader();
			File resource = new File(classLoader.getResource("systemFile/HotelList.json").getFile());
			String HotelListString = new String(Files.readAllBytes(resource.toPath()));

			JSONParser jsonParser = new JSONParser(HotelListString);
			Map<String, Object> obj = jsonParser.parseObject();
			String fileDate =  obj.get("UpdateTime").toString();

			if(hotelListUpdateTimeList.size()>0){
				hotelUpdateTime = hotelListUpdateTimeList.get(0);
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date lastDbUpdate = formatter1.parse(hotelUpdateTime.getValue());
				Date fileUpdateTime = formatter1.parse(fileDate);
				updateData = fileUpdateTime.compareTo(lastDbUpdate) > 0;
			}

			if(updateData){
				ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>> ) obj.get("List");
				log.info(fileDate);

				for(Map<String, Object> hotel : list){
					Integer hotelId =  Integer.parseInt(hotel.get("HotelID").toString());
					log.info("Json hotel id:"+hotelId);
					List<Hotel> existHotel = hotelRepository.findByJsonFileId(hotelId);

					Hotel upsertHotel = null;
					if(existHotel.size() <= 0){
						upsertHotel = new Hotel();
						upsertHotel.setJsonFileId(hotelId);
						log.info("new hotel id:"+hotelId);

					}else{
						upsertHotel = existHotel.get(0);
						log.info("existHotel id:"+hotelId);
					}

					upsertHotel.setLocality(hotel.get("Locality").toString());
					upsertHotel.setAddress(hotel.get("Street-Address").toString());
					upsertHotel.setName(upsertHotel.getLocality()+hotelId);
					Integer hotelStar = Integer.parseInt(hotel.get("HotelStar").toString());
					upsertHotel.setStar(hotelStar);

					ArrayList<Map<String, Object>> rooms = (ArrayList<Map<String, Object>> )hotel.get("Rooms");

					ArrayList<HotelRoom> updateRooms = new ArrayList<HotelRoom>();
					for(Map<String, Object> room : rooms){
						String roomType = room.get("RoomType").toString();
						RoomType rtCode = RoomType.valueOf(roomType);

						HotelRoom upsertRoom = null;
						List<HotelRoom> existHotelRoom = hotelRoomRepository.findByHotelIdAndRoomType(hotelId, rtCode.number);

						if(existHotelRoom.size() <= 0){
							upsertRoom = new HotelRoom();
							upsertRoom.setRoomType(rtCode);
							upsertRoom.setHotelId(hotelId);
						}else {
							upsertRoom = existHotelRoom.get(0);
						}
						Integer price = Integer.parseInt( room.get("RoomPrice").toString());
						upsertRoom.setPrice(price);
						Integer quantity = Integer.parseInt( room.get("Number").toString());
						upsertRoom.setQuantity(quantity);

						updateRooms.add(upsertRoom);
					}

					upsertHotel = hotelRepository.save(upsertHotel);
					hotelRoomRepository.saveAll(updateRooms);

					log.info("add/update one hotel, id:"+ upsertHotel.getId());
				}
			}

			if(hotelUpdateTime == null){
				hotelUpdateTime = new SystemConfig();
				hotelUpdateTime.setName(SystemConstant.ConfigHotelListUpdateTime);
				hotelUpdateTime.setExplanation("上次旅館更新時間");
			}

			hotelUpdateTime.setValue(fileDate);
			systemConfigRepository.save(hotelUpdateTime);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}