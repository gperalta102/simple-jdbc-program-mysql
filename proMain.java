package com.Classname.databasesys;
 import java.sql.* ;  // for standard JDBC programs
 import java.math.* ; // for BigDecimal and BigInteger support
 import java.util.Scanner;


public class proMain {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("hello world");
        //databaseConnect();

        System.out.println("Please select the option that you would like to do");
        System.out.println("1. Display the schedule of all trips for given start location name and destination name and date");
        System.out.println("2. Edit the schedule");
        System.out.println("3. Display the stops of a given trip");
        System.out.println("4 Display the weekly schedule of a given driver and date");
        System.out.println("5 Add Driver");
        System.out.println("6 Add bus");
        System.out.println("7 Delete bus");
        System.out.println("8 Record data of a given trip");

        int userInput = scan.nextInt();
        scan.nextLine();
        switch(userInput){
            case 1:
                System.out.print("Enter start destination: ");
                String start;
                start = scan.nextLine();
                System.out.println(start + " was chosen");
                System.out.print("Enter ending destination: ");
                String stop;
                stop = scan.nextLine();
                System.out.println(stop+" Was Chosen");
                System.out.print("Enter Date: ");
                String datetrip;
                datetrip = scan.nextLine();
                System.out.println("Date Chosen: "+ datetrip);
                gettripinfo(start,stop,datetrip);
                break;
            case 2:

                break;
            case 3:
                System.out.println("Please enter the trip number...");
                int input = scan.nextInt();
                getstops(input);
                break;
            case 4:
                System.out.println("Please enter Driver name");
                String str1 = scan.next();
                System.out.println("Driver Name"+str1);
                System.out.println("Please enter date");
                String str2 = scan.next();
                System.out.println("Date entered: "+str2);

                getSchedule(str1,str2);
                break;
            case 5:
                System.out.println("Please enter the Driver Name");
                String str5 = scan.next();
                System.out.println("Driver Name"+str5);
                System.out.println("Please enter the phone number");
                String str55 = scan.next();
                System.out.println("Phone Number entered: "+str55);
                insertDriver(str5,str55);
                break;
            case 6:
                System.out.print("Please enter Bus ID: ");
                int id = scan.nextInt();
                System.out.println(id);
                System.out.print("Enter Model of Bus: ");
                String carModel = scan.next();
                System.out.println(carModel);
                System.out.print("Enter Model Year: ");
                int modelYear = scan.nextInt();
                System.out.println(modelYear);
                insertBus(id,carModel,modelYear);
                break;
            case 7:
                System.out.print("Please enter Bus ID: ");
                int deleteid = scan.nextInt();
                System.out.println(deleteid);
                deletebus(deleteid);
                System.out.println("Bus has been deleted..");
                break;
            case 8:
                System.out.println("enter trip number ");
                int tripnum = scan.nextInt();
                scan.nextLine();
                System.out.println("enter trip date");
                String tripdate = scan.nextLine();
                System.out.println("Start time: ");
                String starttime = scan.next();
                System.out.println("Stop Number: ");
                int stopnum = scan.nextInt();
                scan.nextLine();
                System.out.println("ArrivalTime: ");
                String arrivaltime = scan.nextLine();
                System.out.println("enter actual start time");
                String actstarttime = scan.nextLine();
                System.out.println("enter actual arrival time");
                String actarrivaltime = scan.nextLine();
                System.out.println("Passengers in: ");
                int passIn = scan.nextInt();
                System.out.println("Passengers out:");
                int passOut = scan.nextInt();
                insertactualtrip(tripnum,tripdate,starttime,stopnum,arrivaltime,actstarttime,actarrivaltime,passIn,passOut);


            default:
                System.out.println("There was an error in the input");
        }


    }

    static void getstops(int x) {
        /////////////////////////////////
        Connection connect = null;
        Statement state = null;
        //register drivers
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
        try {
            //attempt a connection
            System.out.println("Trying to talk to database...");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "password");
            //query



        /////////////////////////////////

            System.out.println("Your input was: " + x);
            PreparedStatement ps = null;
            String sqlinput = "select s.stopaddress from pomona_transit_system.tripstopinfo t, pomona_transit_system.stop s where t.tripnumber = ? and s.stopnumber = t.stopnumber;";
            ps = connect.prepareStatement(sqlinput);
            ps.setInt(1,x);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                String stopAddress = result.getString("stopAddress");
                System.out.println("The Stops are as follows:");
                System.out.println("Stop: " + stopAddress);

            }
            result.close();


        }catch (SQLException see){ see.printStackTrace();}
        try{
            if(state!=null)
                connect.close();
        }catch(SQLException se){
        }// do nothing
        try{
            if(connect!=null)
                connect.close();
        }catch(SQLException se){
            se.printStackTrace();
        }//end finally try
    }
    static void getSchedule(String name,String date){

        /////////////////////////////////
        Connection connect = null;
        Statement state = null;
        //register drivers
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
        try {
            //attempt a connection
            System.out.println("Trying to talk to database...");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "password");
            //query



            /////////////////////////////////

            //System.out.println("Your input was: " + x);
            PreparedStatement ps = null;
            String sqlinput = "select t.scheduledstarttime, t.scheduledarrivaltime, t.drivername, t.tripdate\n" +
                    "from pomona_transit_system.tripoffering t, pomona_transit_system.driver d\n" +
                    "where t.drivername=d.drivername and d.drivername=? and tripdate=?;";
            ps = connect.prepareStatement(sqlinput);
            ps.setString(1,name);
            ps.setString(2,date);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                String scheduledStartTime = result.getString("scheduledstarttime");
                String scheduledarrivaltime = result.getString("scheduledarrivaltime");
                String drivername = result.getString("drivername");
                String tripdate = result.getString("tripdate");
                System.out.println("");
                System.out.println("Start time: "+ scheduledStartTime);
                System.out.println("Arrival Time: "+ scheduledarrivaltime);
                System.out.println("Driver Name: "+drivername);
                System.out.println("Trip Date: "+tripdate);

            }
            result.close();


        }catch (SQLException see){ see.printStackTrace();}
        try{
            if(state!=null)
                connect.close();
        }catch(SQLException se){
        }// do nothing
        try{
            if(connect!=null)
                connect.close();
        }catch(SQLException se){
            se.printStackTrace();
        }//end finally try


    }
    static void insertDriver(String name, String phone){

        /////////////////////////////////
        Connection connect = null;
        Statement state = null;
        //register drivers
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
        try {
            //attempt a connection
            System.out.println("Trying to talk to database...");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "password");
            //query



            /////////////////////////////////

            //System.out.println("Your input was: " + x);
            PreparedStatement ps = null;
            String sqlinput = "insert into pomona_transit_system.driver values(?,?);";

            ps = connect.prepareStatement(sqlinput);
            ps.setString(1,name);
            ps.setString(2,phone);
            ps.execute();


            state = connect.createStatement();
            ResultSet result = state.executeQuery("select * from pomona_transit_system.driver");


            while (result.next()) {
                String driverName = result.getString("drivername");
                String drivertelephonenumber = result.getString("drivertelephonenumber");

                System.out.println("");
                System.out.println("Driver Name: "+ driverName);
                System.out.println("Driver's Phone Number: "+ drivertelephonenumber);

            }
            result.close();


        }catch (SQLException see){ see.printStackTrace();}
        try{
            if(state!=null)
                connect.close();
        }catch(SQLException se){
        }// do nothing
        try{
            if(connect!=null)
                connect.close();
        }catch(SQLException se){
            se.printStackTrace();
        }//end finally try


    }
    static void insertBus(int busId, String modelname, int year){

        /////////////////////////////////
        Connection connect = null;
        Statement state = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
        try {
            //attempt a connection
            System.out.println("Trying to talk to database...");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "password");
            //query



            /////////////////////////////////

            //System.out.println("Your input was: " + x);
            PreparedStatement ps = null;
            String sqlinput = "insert into pomona_transit_system.bus values(?,?,?);";

            ps = connect.prepareStatement(sqlinput);
            ps.setInt(1,busId);
            ps.setString(2,modelname);
            ps.setInt(3,year);
            ps.execute();


            state = connect.createStatement();
            ResultSet result = state.executeQuery("select * from pomona_transit_system.bus");


            while (result.next()) {
                int busidnumber = result.getInt("busId");
                String modeldisplay = result.getString("model");
                String modelyear = result.getString("year");

                System.out.println("");
                System.out.println("Bus ID: "+ busidnumber);
                System.out.println("Model: "+ modeldisplay);
                System.out.println("Model Year: "+modelyear);

            }
            result.close();


        }catch (SQLException see){ see.printStackTrace();}
        try{
            if(state!=null)
                connect.close();
        }catch(SQLException se){
        }// do nothing
        try{
            if(connect!=null)
                connect.close();
        }catch(SQLException se){
            se.printStackTrace();
        }//end finally try


    }
    static void deletebus(int input){

        /////////////////////////////////
        Connection connect = null;
        Statement state = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
        try {
            //attempt a connection
            System.out.println("Trying to talk to database...");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "password");
            //query



            /////////////////////////////////

            //System.out.println("Your input was: " + x);
            PreparedStatement ps = null;
            String sqlinput = "delete from pomona_transit_system.bus where busId =?;";

            ps = connect.prepareStatement(sqlinput);
            ps.setInt(1,input);
            ps.execute();


            state = connect.createStatement();
            ResultSet result = state.executeQuery("select * from pomona_transit_system.bus");


            while (result.next()) {
                int busidnumber = result.getInt("busId");
                String modeldisplay = result.getString("model");
                String modelyear = result.getString("year");

                System.out.println("");
                System.out.println("Bus ID: "+ busidnumber);
                System.out.println("Model: "+ modeldisplay);
                System.out.println("Model Year: "+modelyear);

            }
            result.close();


        }catch (SQLException see){ see.printStackTrace();}
        try{
            if(state!=null)
                connect.close();
        }catch(SQLException se){
        }// do nothing
        try{
            if(connect!=null)
                connect.close();
        }catch(SQLException se){
            se.printStackTrace();
        }//end finally try


    }
    static void gettripinfo(String begin,String end, String datechosen){

        /////////////////////////////////
        Connection connect = null;
        Statement state = null;
        //register drivers
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
        try {
            //attempt a connection
            System.out.println("Trying to talk to database...");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "password");
            //query



            /////////////////////////////////

            //System.out.println("Your input was: " + x);
            PreparedStatement ps = null;
            String sqlinput = "select distinct t.*, a.scheduledstarttime, a.scheduledarrivaltime, tt.drivername, tt.busid\n" +
                    "from pomona_transit_system.trip t, pomona_transit_system.actualtripstopinfo a, pomona_transit_system.bus b, pomona_transit_system.driver d, pomona_transit_system.tripoffering tt\n" +
                    "where t.startlocationname = ? and t.destinationName = ? and a.tripdate=? and\n" +
                    "tt.tripnumber = t.tripnumber;";
            ps = connect.prepareStatement(sqlinput);
            ps.setString(1,begin);
            ps.setString(2,end);
            ps.setString(3,datechosen);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                String tripnumber = result.getString("tripnumber");
                String startlocation = result.getString("startlocationname");
                String destinationname = result.getString("destinationname");
                String scheduledstarttime = result.getString("scheduledstarttime");
                String scheduledarrivaltime = result.getString("scheduledarrivaltime");
                String drivername = result.getString("drivername");
                String bisid = result.getString("busid");
                System.out.println("");
                System.out.println("Trip number: "+tripnumber);
                System.out.println("Start location: "+startlocation);
                System.out.println("Destination: "+ destinationname);
                System.out.println("Scheduled Start Time: "+ scheduledstarttime);
                System.out.println("Scheduled Arrival Time: "+ scheduledarrivaltime);
                System.out.println("Driver Name: "+ drivername);
                System.out.println("Bus ID: "+bisid);

            }
            result.close();


        }catch (SQLException see){ see.printStackTrace();}
        try{
            if(state!=null)
                connect.close();
        }catch(SQLException se){
        }// do nothing
        try{
            if(connect!=null)
                connect.close();
        }catch(SQLException se){
            se.printStackTrace();
        }//end finally try


    }
    static void insertactualtrip(int tripnum,String tripdate,String starttime, int stopnum, String arrivaltime, String actstarttime, String actarrivaltime,int passIn, int passOut){

        /////////////////////////////////
        Connection connect = null;
        Statement state = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
        try {
            //attempt a connection
            System.out.println("Trying to talk to database...");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "password");
            //query



            /////////////////////////////////

            //System.out.println("Your input was: " + x);
            PreparedStatement ps = null;
            String sqlinput = "insert into pomona_transit_system.actualtripstopinfo values(?, ?, ?, ?, ?, ?, ?, ?, ? );\n";

            ps = connect.prepareStatement(sqlinput);
            ps.setInt(1,tripnum);
            ps.setString(2,tripdate);
            ps.setString(3,starttime);
            ps.setInt(4,stopnum);
            ps.setString(5,arrivaltime);
            ps.setString(6,actstarttime);
            ps.setString(7,actarrivaltime);
            ps.setInt(8,passIn);
            ps.setInt(9,passOut);
            ps.execute();

            System.out.println("Information Is added");








        }catch (SQLException see){ see.printStackTrace();}
        try{
            if(state!=null)
                connect.close();
        }catch(SQLException se){
        }// do nothing
        try{
            if(connect!=null)
                connect.close();
        }catch(SQLException se){
            se.printStackTrace();
        }//end finally try


    }



    }




