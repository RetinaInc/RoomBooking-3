import java.util.ArrayList;
/**
 * A class which creates and manages the rooms
 */
public class RoomManager {
	/**
	 * Constructs a RoomManager object
	 */
	public RoomManager() {
		roomArray = new ArrayList<Room>();
	}
	
	/**
	 * Creates a new room with the specified details
	 * @param details Details of the room to be created
	 */
	public void newRoom (String details) {
		command = details.split(" ");
		int capacity = Integer.parseInt(command[1]);
		String name = command[2];
		
		Room newRoom = new Room(capacity, name);
		roomArray.add(newRoom);
	}
	
	/**
	 * Prints the details of the room
	 * @param details Name of the room of concern
	 */
	public void print (String details) {
		command = details.split(" ");
		String rName = command[1];
		for (Room r : roomArray) {
			if (  rName.equals(r.roomName()) ) {
				r.printReservations();
			}
		}
	}
	/**
	 * Books a room 
	 * @param details Booking details 
	 * @return Return true if booking is successful or false if unsuccessful
	 */
	public boolean book (String details) {
		
		command = details.split(" ");
		String name = command[1];
		int capacity = Integer.parseInt(command[2]);
		int numweeks = Integer.parseInt(command[3]);
		String month = command[4];
		int date = Integer.parseInt(command[5]);
		int time = Integer.parseInt(command[6]);
		int duration = Integer.parseInt(command[7]);
		String title = command[8];
		
		//finding a free doom
		for (int i = 0; i < roomArray.size() && i != -1; i++) {
			//size check
			if (roomArray.get(i).roomSize() >= capacity) {
				//if room is available at given date(s) and times then book
				int j;
				for (j = 0; j != -1 && j < numweeks; j++) {
					if (!roomArray.get(i).isAvailable(month, date + j*7, time, duration)) {						
						j = -2;
					}
				}
				if (j != -1) {
						roomArray.get(i).addReservation
						(name, title, numweeks, month, date, time, duration);
						String roomName = roomArray.get(i).roomName();
						System.out.println("Room " + roomName + " assigned");	
						return true;
				}		
			}
		}
		System.out.println("Booking rejected");
		return false;
	}
		
	/**
	 * Returns the number of rooms in the roomManager
	 * @return number of rooms
	 */
	public int numberOfRooms() {
		return roomArray.size();
	}	
	
	
	/**
	 * Deletes a booking
	 * @param details Details of the booking(s) to be deleted
	 * @return Returns true if deletion is successful, else return false
	 */
	public boolean delete (String details) {
		command = details.split(" ");
		String name = command[1];
		String room = command[2];
		int numweeks = Integer.parseInt(command[3]);
		String month = command[4];
		int date = Integer.parseInt(command[5]);
		int time= Integer.parseInt(command[6]);

		int i;
		Room targetRoom = null;
		for (i = 0; i < roomArray.size() && i != -1; i++) {
			if (roomArray.get(i).roomName().equals(room)) {
				targetRoom = roomArray.get(i);
				i = -2;
			}			
		}
		
		if (i != -1) {
			return false;
		}
		
		int j = 0;
		int numWeeks = numweeks;
		while (numWeeks != 0) {
			if (!targetRoom.isOwnedBy(month, date + j*7, time, name)) {
				return false;
			}
			j++;
			numWeeks--;
		}
			
		int k = 0;
		while (numweeks != 0) {
			targetRoom.deleteRoom(name, month, date + k*7, time);
			k++;
			numweeks--;
		}
		return true;		
	}
	
	/**
	 * Changes reservation
	 * @param details Details of original and new reservation
	 * @return Return true if successful, else false
	 */
	public boolean change (String details) {
		
		command = details.split(" ");
		String name = command[1];
		String room = command[2];
		int numweeks = Integer.parseInt(command[3]);
		String month1 = command[4];
		int date1 = Integer.parseInt(command[5]);
		int time1 = Integer.parseInt(command[6]);
		
		int capacity = Integer.parseInt(command[7]);
		
		String month2 = command[8];
		int date2 = Integer.parseInt(command[9]);
		int time2 = Integer.parseInt(command[10]);
		int duration2 = Integer.parseInt(command[11]);
		String title = command[12];
		
		//check if reservations exist
		//check if room is unavaible for said dates and times
		Room targetRoom = null;
		for (int i = 0; i < roomArray.size() && i != -1; i++) {
			if (roomArray.get(i).roomName().equals(room)) {
				targetRoom = roomArray.get(i);
				i = -2;
			}			
		}
		
		if (targetRoom == null) {
			System.out.println("Changed rejected");
			return false;
		}
		
		int j;
		for (j = 0; j != -1 && j < numweeks; j++) {
			//reservation exists & owned by user
			if (targetRoom.isAvailable(month1, date1 + j*7, time1, 0) ||
					!targetRoom.isOwnedBy(month1, date1 + j*7, time1, name)) {						
				System.out.println("Changed rejected");
				return false;
			}
		}	
		
		for (int i = 0; i < roomArray.size() && i != -1; i++) {
			//size check
			if (roomArray.get(i).roomSize() >= capacity) {
				//if room is avaialable at given date(s) and times then book
				for (j = 0; j != -1 && j < numweeks; j++) {
					if (!roomArray.get(i).isAvailable(month2, date2 + j*7, time2, duration2)) {						
						j = -2;
					}
				}
				
				if (j != -1) { //if available for said times
					String deletionString = String.format("Delete %s %s %d %s %d %d", name, room, numweeks, month1, date1, time1);
					this.delete(deletionString);
					roomArray.get(i).addReservation
					(name, title, numweeks, month2, date2, time2, duration2);
					System.out.println("Room " + roomArray.get(i).roomName() + " assigned");
					return true;			
				}		
			}
		}
		System.out.println("Changed rejected");
		return false;
	}

	private ArrayList<Room> roomArray;
	private String[] command;
}