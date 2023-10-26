/*
 * BooksDatabaseService.java
 *
 * The service threads for the books database server.
 * This class implements the database access service, i.e. opens a JDBC connection
 * to the database, makes and retrieves the query, and sends back the result.
 *
 * author: <ZXQ190>
 *
 */

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
//import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.Socket;

import java.util.StringTokenizer;

import java.sql.*;
import javax.sql.rowset.*;
//Direct import of the classes CachedRowSet and CachedRowSetImpl will fail becuase
//these clasess are not exported by the module. Instead, one needs to impor
//javax.sql.rowset.* as above.


public class BooksDatabaseService extends Thread {

    private Socket serviceSocket = null;
    private String[] requestStr = new String[2]; //One slot for author's name and one for library's name.
    private ResultSet outcome = null;


    //JDBC connection
    private String USERNAME = Credentials.USERNAME;
    private String PASSWORD = Credentials.PASSWORD;
    private String URL = Credentials.URL;


    //Class constructor
    public BooksDatabaseService(Socket aSocket) {

        //TO BE COMPLETED //DONE
        this.serviceSocket = aSocket;
        this.start();
    }


    //Retrieve the request from the socket
    public String[] retrieveRequest() {
        this.requestStr[0] = ""; //For author
        this.requestStr[1] = ""; //For library

        String tmp = "";
        try {

            //TO BE COMPLETED //DONE
            InputStream outComeStream = serviceSocket.getInputStream();
            InputStreamReader outComeStreamReader = new InputStreamReader(outComeStream);
            StringBuffer stringBuffer = new StringBuffer();
            char x;
            while (true)
            {
                x = (char) outComeStreamReader.read();
                if (x == '#')
                    break;
                stringBuffer.append(x);
            }
            tmp = stringBuffer.toString();
            this.requestStr = tmp.split(";");
        } catch (IOException e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        return this.requestStr;
    }


    //Parse the request command and execute the query
    public boolean attendRequest() {
        boolean flagRequestAttended = true;

        this.outcome = null;

        String sql = "SELECT book.title AS \"Title\",book.publisher AS \"Publisher\", book.genre AS \"Genre\", book.rrp AS \"RRP\", COUNT(*) AS \"Num. Copies\" FROM bookcopy INNER JOIN book ON book.bookid = bookcopy.bookid INNER JOIN author ON book.authorid = author.authorid INNER JOIN library ON library.libraryid = bookcopy.libraryid WHERE author.familyname = ? AND library.city = ? GROUP BY book.title,book.publisher,book.genre,book.rrp HAVING COUNT(*) > 0;"; //TO BE COMPLETED- Update this line as needed.

        try {
            //Connet to the database
            //TO BE COMPLETED //DONE
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            //Make the query
            //TO BE COMPLETED //DONE
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.clearParameters();
            preparedStatement.setString(1, this.requestStr[0]);
            preparedStatement.setString(2, this.requestStr[1]);

            //Process query
            //TO BE COMPLETED -  Watch out! You may need to reset the iterator of the row set.
            ResultSet rs = preparedStatement.executeQuery();
            RowSetFactory aFactory = RowSetProvider.newFactory();

            CachedRowSet crs = aFactory.createCachedRowSet();

            crs.populate(rs);
            this.outcome = crs;


            while (this.outcome.next()){
                System.out.println(outcome.getString("Title") + " | " + outcome.getString("Publisher") +
                        " | " + outcome.getString("Genre") + " | " + outcome.getString("RRP") + " | "	+
                        outcome.getString("Num. Copies"));

            }
            crs.beforeFirst();


            //Clean up
            //TO BE COMPLETED
            rs.close();
            preparedStatement.close();
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return flagRequestAttended;
    }


    //Wrap and return service outcome
    public void returnServiceOutcome() {
        try {
            //Return outcome
            //TO BE COMPLETED

            ObjectOutputStream outStream = new ObjectOutputStream(serviceSocket.getOutputStream());
            outStream.writeObject(outcome);
            outStream.flush();

            System.out.println("Service thread " + this.getId() + ": Service outcome returned; " + this.outcome);

            //Terminating connection of the service socket
            //TO BE COMPLETED
            serviceSocket.close();

        } catch (IOException e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
    }


    //The service thread run() method
    public void run() {
        try {
            System.out.println("\n============================================\n");
            //Retrieve the service request from the socket
            this.retrieveRequest();
            System.out.println("Service thread " + this.getId() + ": Request retrieved: "
                    + "author->" + this.requestStr[0] + "; library->" + this.requestStr[1]);

            //Attend the request
            boolean tmp = this.attendRequest();

            //Send back the outcome of the request
            if (!tmp)
                System.out.println("Service thread " + this.getId() + ": Unable to provide service.");
            this.returnServiceOutcome();

        } catch (Exception e) {
            System.out.println("Service thread " + this.getId() + ": " + e);
        }
        //Terminate service thread (by exiting run() method)
        System.out.println("Service thread " + this.getId() + ": Finished service.");
    }

}
