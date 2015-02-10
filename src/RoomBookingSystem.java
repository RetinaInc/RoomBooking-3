import java.util.Scanner;
import java.io.*;
/**
 * A class which acts as the interface between user and roomBookingSystem
 * @author Jimmy Qian
 *
 */

public class RoomBookingSystem {
	public static void main(String[] args) {

		RoomManager manager = new RoomManager();
		
		File newFile= new File(args[0]);
		Scanner sc = null;
		
		try {
			sc = new Scanner(newFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		}
		
		while (sc.hasNextLine()) {
			String userCommand = sc.nextLine();
			
			if (userCommand.startsWith("Room")) {
				manager.newRoom(userCommand);
			} 
			
			else if (userCommand.startsWith("Book")) {
				manager.book(userCommand);
			} 
			
			else if (userCommand.startsWith("Change")) {
				manager.change(userCommand);
			} 
			
			else if (userCommand.startsWith("Delete")) {
				if(manager.delete(userCommand)) {
					System.out.println("Reservations deleted");
				} else {
					System.out.println("Deletion rejected");
				}
			} 
			
			else if (userCommand.startsWith("Print")) {
				manager.print(userCommand);
			} 
			
			else if (userCommand.startsWith("NumberOfRooms")) {
				manager.numberOfRooms();
			}
		}
		
	}
}