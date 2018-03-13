package nz.ac.massey;

import java.io.IOException;

/*
 * @author: 16058989, Dylan Tonks
 * @course 159.352 Advanced Web Development
 * @university Massey University
 * 
 * Pre requirements: a folder "./resources"
 * this is for all your files. No sub directories please.
 */
public class Runner {

    public static void main(String[] args) {

	    try {
	        Thread t = new Server("127.0.0.1", 5000);
	        t.start();
        }
        catch(IOException e) {
	        e.printStackTrace();
        }
    }
}
