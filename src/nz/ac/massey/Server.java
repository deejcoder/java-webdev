package nz.ac.massey;

import java.net.*;
import java.io.*;


public class Server extends Thread {

    private String host;
    private int port;
    private int bufferSize = 4096;

    private ServerSocket socket;

    public Server(String host, int port) throws IOException {
        this.host = host;
        this.port = port;

        SocketAddress ip = new InetSocketAddress(host, port);
        socket = new ServerSocket();

        //Bind the IP to the newly created socket.
        socket.bind(ip);
    }

    @Override
    public void run() {

        try {
            while (true) {

                //Accept an incoming client
                Socket server = socket.accept();

                //Get the data streams associated with the socket
                DataInputStream in = new DataInputStream(server.getInputStream());
                BufferedOutputStream bout = new BufferedOutputStream(server.getOutputStream());

                //Get the data coming from the client
                byte[] bytes = new byte[bufferSize];
                in.read(bytes, 0, bufferSize);
                String data = new String(bytes, "UTF-8");
                System.out.println(data);

                //Get the name of the resource the client is requesting
                String lines = data.split(System.lineSeparator(), 2)[0];
                String file = lines.split(" ", 3)[1];

                if(file.equals("/")) {
                    file = "/index.html";
                }


                /*
                    Respond to the client with the requested resource.
                    If there is no requested resource, return a 404 HTML page.
                 */
                try {
                    //IOException if cannot get file
                    byte[] fbytes = getResource(file);

                    String ext = file.split("\\.", 2)[1];

                    bout.write("HTTP/2.0 200 OK\r\n".getBytes());

                    switch(ext) {
                        case "png": bout.write("Content-Type: text/png\r\n".getBytes());break;
                        case "gif": bout.write("Content-Type: text/gif\r\n".getBytes());break;
                        case "jpg": bout.write("Content-Type: text/jpeg\r\n".getBytes());break;
                        case "html": bout.write("Content-Type: text/html\r\n".getBytes());break;
                        case "css": bout.write("Content-Type: text/css\r\n".getBytes());break;
                    }

                   // bout.write("cache-control: private, max-age=31536000\r\n".getBytes());
                    //bout.write(("last-modified: " + getLastModifiedFile(file)).getBytes());
                    bout.write("\r\n".getBytes());
                    bout.write(fbytes);

                }

                //Return 404 Not Found
                catch(IOException ie) {
                    bout.write("HTTP/1.0 404 Not Found\r\n".getBytes());


                    try {
                        byte[] fbytes = getResource("/404.html");
                        bout.write("Content-Type: text/html\r\n".getBytes());
                        bout.write("\r\n".getBytes());
                        bout.write(fbytes);
                    }
                    catch(IOException iee) {
                        bout.write("\r\n".getBytes());
                        bout.write("<p>Uh...there's no 404 page.</p>".getBytes());
                    }
                }

                //Flush & close the connection
                bout.flush();
                server.close();
            }
        }
        catch(IOException ie) {
            ie.printStackTrace();
        }
    }

    public byte[] getResource(String fileString) throws IOException {
        File file = new File(("./resources" + fileString));
        String ext = fileString.split("\\.", 2)[1];
        byte[] bytes = new byte[(int)file.length()];

        FileInputStream fin = new FileInputStream("./resources" + fileString);
        BufferedInputStream bin = new BufferedInputStream(fin);
        bin.read(bytes, 0, bytes.length);
        return bytes;
    }

    public long getLastModifiedFile(String fileString) throws IOException {
        File file = new File("./resources" + fileString);
        return file.lastModified();
    }
}
