package nz.ac.massey;

import java.io.IOException;

public class Runner {

    public static void main(String[] args) {
        System.out.println(0x0d);
	    try {
	        Thread t = new Server("127.0.0.1", 5000);
	        t.start();
        }
        catch(IOException e) {
	        e.printStackTrace();
        }
    }
}
