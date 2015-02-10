import java.util.ArrayList;
/**
 * A class for managing the reservation details of rooms
 */
public class Room {
	
	/**
	 * Constructs a room
	 * @param c Caoacity of room
	 * @param n Name of room
	 */
	public Room (int c, String n) {	
		capacity = c;
		name = n;
		reservationTime = new ArrayList<int[]>();
		reservationDate = new ArrayList<Integer>();
		reservationNameAndPurpose= new ArrayList<String[]>();
	}
	/**
	 * Returns name of room
	 * @return Name of room as a string
	 */
	public String roomName() {
		return name;
	}
	
	/**
	 * Returns size of room
	 * @return Size of room as int
	 */
	public int roomSize() {
		return capacity;
	}
	
	/**
	 * Adds reservation to room 
	 * @param guestName Name of user
	 * @param purpose Purpose of room
	 * @param numweeks Number of weeks to book for
	 * @param month Month of booking
	 * @param date Date of booking
	 * @param time Time of booking
	 * @param duration Duration of booking
	 */
	public void addReservation (String guestName, String purpose, int numweeks, String month, int date, int time, int duration) {
		
		for (int i = 0; i < numweeks; i++) { 
			int ddate = convert(month, date) + i*7;
			// adding each room 
			int[] ttime = new int[2];
			ttime[0] = time;
			ttime[1] = time + duration;
			
			String[] nameAndPurpose = new String[2];
			nameAndPurpose[0] = guestName;
			nameAndPurpose[1] = purpose;
			
			//DETERMINING POSISTION OF ENTRY
			int index = 0;
			int stop = 0;
			if (reservationDate.size() == 0) {
			} 
			
			// if 1 entry then
			else if (reservationDate.size() == 1) { 
				if (toPosition(ddate, ttime[0]) > toPosition(reservationDate.get(0), reservationTime.get(0)[0])) {
					index = 1; 
				}
			} 
			
			// 2 or more entries present
			else {
				// if it's entry 0
				if (toPosition(ddate, ttime[0]) < toPosition(reservationDate.get(0), reservationTime.get(0)[0])) {
				} 
				//if it's the last entry
				else if (toPosition(ddate, ttime[0]) > toPosition(reservationDate.get(reservationDate.size()-1), reservationTime.get(reservationTime.size()-1)[0])) {
					index = reservationDate.size();
				} 
				 // if it's not first and not last
				else {
					while (stop != 1) {
						if (toPosition(ddate, ttime[0])   >   toPosition(reservationDate.get(index), reservationTime.get(index)[0])  
							&&
							toPosition(ddate, ttime[0])   <   toPosition(reservationDate.get(index+1), reservationTime.get(index+1)[0])
						) {
							index++;
							stop = 1;		
						}
					else {
						index++;
					}
				}
			}
			}
			reservationTime.add(index, ttime);
			reservationDate.add(index, ddate);		
			reservationNameAndPurpose.add(index, nameAndPurpose);
		}
	}

	/**
	 * Prints all reservations for the room
	 */
	public void printReservations() {
		System.out.println(name);
		if (reservationNameAndPurpose.size() == 0) {
			System.out.println("No Bookings");
		} 
		else {
			int i = 0;
				for (String[] s: reservationNameAndPurpose) {
					String guestName = s[0];
					String purpose = s[1];
					String date = dateToString(reservationDate.get(i));
					int from = reservationTime.get(i)[0];
					int to = reservationTime.get(i)[1];
					System.out.println(guestName + " " + date + " " + from  + " " + (to - from) + " " + purpose);
					i++;
				}	
		}
	}
	
	/**
	 * Deletes booking if it exists
	 * @param guest Name of user
	 * @param month Month of booking
	 * @param date Date of booking
	 * @param time Time of booking
	 */
	public void deleteRoom (String guest, String month, int date, int time) {
		for (int i = 0; i < reservationDate.size(); i++) {
			if (reservationDate.get(i) == (convert(month, date)) &&
				reservationTime.get(i)[0] == time) {		
				reservationDate.remove(i);
				reservationTime.remove(i);
				reservationNameAndPurpose.remove(i);
				return;
			}
		}
	}
	
/**
 * Checks whether a booking exists
 * @param month Month of booking
 * @param date Date of booking
 * @param time Time of booking
 * @param duration Duration of booking
 * @return True if exists, else false
 */
	public boolean isAvailable (String month, int date, int time, int duration ) {
		if (!reservationDate.contains(convert(month, date))) {
			return true; 
		} 
		//otherwise date is taken one or many times
		//check every time date is booked, whether time is free or not
		for (int j = 0; j < reservationDate.size(); j++) {
			//if date is taken
			if (reservationDate.get(j).equals(convert(month, date))) {
			//if time is taken
				if (!((time < reservationTime.get(j)[0] 
						&& time + duration <= reservationTime.get(j)[0]) ||
						(time >= reservationTime.get(j)[1] 
						&& time + duration > reservationTime.get(j)[1]))) {	
					return false;
				}
			}
		}
		return true;
	} 
		
	/**
	 * Checks whether booking is owned by user
	 * @param month Month of booking
	 * @param date Date of booking
	 * @param time Time of booking
	 * @param name Name to be checked against
	 * @return True if ownership is valid, else false
	 */
	public boolean isOwnedBy(String month, int date, int time, String name) {
		int i = 0;
		while (i < reservationDate.size()) {
			if (reservationDate.get(i) == convert(month, date) 
				&& reservationTime.get(i)[0] == time
				&& reservationNameAndPurpose.get(i)[0].equals(name)) {
				return true;
			}
			i++;
		}
		return false;
	}
	
	/**
	 * Converts date and time to a position in the reservation array
	 * @param date Date of booking
	 * @param time Time of booking
	 * @return Returns position as a double
	 */
	private double toPosition (int date, int time) {
		return (double)date + ((double)time)/100;		
	}
		
	/**
	 * Converts date to string
	 * @param date Date of  booking
	 * @return The date as a string
	 */
	private String dateToString(int date) {
		String s;
		if (date > 0 && date <= 31) {
			s = String.format("Jan %d", date);
		} else if (date > 31 && date <= 59) {
			s = String.format("Feb %d", date-31);
		} else if (date > 59 && date <= 90) {
			s = String.format("Mar %d", date-59);
		} else if (date > 90 && date <= 120) {
				s = String.format("Apr %d", date-90);
		} else if (date > 120 && date <= 151) {
			s = String.format("May %d", date-120);
		} else if (date > 151 && date <= 181) {
			s = String.format("Jun %d", date-151);
		} else if (date > 181 && date <= 212) {
			s = String.format("Jul %d", date-181);
		} else if (date > 212 && date <= 243) {
			s = String.format("Aug %d", date-212);
		} else if (date > 243 && date <= 273) {
				s = String.format("Sep %d", date-243);
		} else if (date > 273 && date <= 304) {
			s = String.format("Oct %d", date-273);
		} else if (date > 304 && date <= 334) {
			s = String.format("Nov %d", date-304);
		} else {
			s = String.format("Dec %d", date-334);
		}
		return s;
	}
	
	/**
	 * Hash function which converts month and date to a unique integer
	 * @param month Month of concern
	 * @param date Date of concern
	 * @return Unique hash value
	 */
	private int convert (String month, int date) {
		int base;
		if ( month.equals("Jan") ){
			base = 0;
		} else if ( month.equals("Feb") ) {
			base = 31;
		} else if ( month.equals("Mar") ) {
			base = 59;
		} else if ( month.equals("Apr") ) {
			base = 90;
		}else if ( month.equals("May") ) {
			base = 120;
		}else if ( month.equals("Jun") ) {
			base = 151;
		}else if ( month.equals("Jul") ) {
			base = 181;
		}else if ( month.equals("Aug") ) {
			base = 212;
		}else if ( month.equals("Sep") ) {
			base = 243;
		}else if ( month.equals("Oct") ) {
			base = 273;
		}else if ( month.equals("Nov") ) {
			base = 304;
		}else {
			base = 334;
		} 
		return base + date;		
	}
	
	private int capacity;
	private String name;
	private ArrayList<int[]> reservationTime;
	private ArrayList<Integer> reservationDate;
	private ArrayList<String[]> reservationNameAndPurpose;
}
