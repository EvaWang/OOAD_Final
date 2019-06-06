import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

public class Hotel {
	@SerializedName("HotelID")
	private int ID;
	@SerializedName("HotelStar")
	private int Star;
	private String Locality;
	@SerializedName("Street-Address")
	private String Address;
	
	@SerializedName("Rooms")
	private RoomType[] RoomTypes;
	private static int SingleRoomPrice;
	private static int DoubleRoomPrice;
	private static int QuadRoomPrice;
	private Room[] SingleRooms, DoubleRooms, QuadRooms;
	
	private ArrayList<Order> Orders;
	
	public void init() {
		SingleRooms = new Room[RoomTypes[0].getNumber()];
		DoubleRooms = new Room[RoomTypes[1].getNumber()];
		QuadRooms = new Room[RoomTypes[2].getNumber()];
		for (int i = 0; i < SingleRooms.length; i++) 
			SingleRooms[i] = new Room();
		for (int i = 0; i < DoubleRooms.length; i++) 
			DoubleRooms[i] = new Room();
		for (int i = 0; i < QuadRooms.length; i++) 
			QuadRooms[i] = new Room();
		SingleRoomPrice = RoomTypes[0].getPrice();
		DoubleRoomPrice = RoomTypes[1].getPrice();
		QuadRoomPrice = RoomTypes[2].getPrice();
		Orders = new ArrayList<Order>();
	}
	public int getID() {
		return ID;
	}
	public int getStar() {
		return Star;
	}
	public String getLocality() {
		return Locality;
	}
	public String getAddress() {
		return Address;
	}
	Room[] getSingleRooms() {
		/*Room[] nSingleRooms = new Room[SingleRooms.length];
		for (int i = 0; i < nSingleRooms.length; i++) 
			nSingleRooms[i] = new Room(SingleRooms[i]);
		return nSingleRooms;*/
		return SingleRooms;
	}
	Room[] getDoubleRooms() {
		/*Room[] nDoubleRooms = new Room[DoubleRooms.length];
		for (int i = 0; i < nDoubleRooms.length; i++) 
			nDoubleRooms[i] = new Room(DoubleRooms[i]);
		return nDoubleRooms;*/
		return DoubleRooms;
	}
	Room[] getQuadRooms() {
		/*Room[] nQuadRooms = new Room[QuadRooms.length];
		for (int i = 0; i < nQuadRooms.length; i++) 
			nQuadRooms[i] = new Room(QuadRooms[i]);
		return nQuadRooms;*/
		return QuadRooms;
	}
	public static int getSingleRoomPrice() {
		return SingleRoomPrice;
	}
	public static int getDoubleRoomPrice() {
		return DoubleRoomPrice;
	}
	public static int getQuadRoomPrice() {
		return QuadRoomPrice;
	}
	public ArrayList<Order> getOrders() {
		return Orders;
	}
	public String toString() {
		return ID + " " + Star + " " + Locality + " " + Address + " " 
				+ RoomTypes[0] + " " + RoomTypes[1] + " " + RoomTypes[2];
	}
	public void newOrder(Order _Order) {
		Orders.add(_Order);
	}
}