import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/customer")
    public class CustomerServlet extends HttpServlet {
        // Json Formats
        // String json="{id:C001,name:Dasun,address:Galle,salary:1000}"; //single customer info
        // String jsonSet="[{id:C001,name:Dasun,address:Galle,salary:1000},{id:C001,name:Dasun,address:Galle,salary:1000}]"; //multiple customer info
        //This method can be used to get customer information.
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            try {
                //The Media Type of the Content of the response
                resp.setContentType("application/json"); // MIME Types (Multipurpose Internet Mail Extensions)

                String option = req.getParameter("option");
                //meta data for response from headers
//                resp.addHeader("Institute", "IJSE");
//                resp.addHeader("Course", "GDSE");

                switch (option){
                    case "SEARCH":
                        String customerID = req.getParameter("CustomerID");
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "1234");
                        PreparedStatement preparedStatement = connection.prepareStatement("select * from customer where id=?");
                        preparedStatement.setObject(1,customerID);
                        ResultSet rst = preparedStatement.executeQuery();

                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

                        while (rst.next()) {
                            String id = rst.getString(1);
                            String name = rst.getString(2);
                            String address = rst.getString(3);
                            double salary = rst.getDouble(4);


                            objectBuilder.add("id",id);
                            objectBuilder.add("name",name);
                            objectBuilder.add("address",address);
                            objectBuilder.add("salary",salary);
                        }
                        PrintWriter writer = resp.getWriter();

                        //Generate a custom response with json
                        JsonObjectBuilder response=Json.createObjectBuilder();

                        response.add("status",200);
                        response.add("message","Done");
                        response.add("data",objectBuilder.build());

                        writer.print(response.build());

                        break;
                    case "GETALL":
                        //Initialize the connection
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "1234");
                        ResultSet resultSet = connection.prepareStatement("select * from customer").executeQuery();
                        //String allRecords = "";
                        // Access the records and generate a json object


                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                        while (resultSet.next()) {
                            String id = resultSet.getString(1);
                            String name = resultSet.getString(2);
                            String address = resultSet.getString(3);
                            double salary = resultSet.getDouble(4);

                            JsonObjectBuilder objectBuilder1 = Json.createObjectBuilder();

                            objectBuilder1.add("id",id);
                            objectBuilder1.add("name",name);
                            objectBuilder1.add("address",address);
                            objectBuilder1.add("salary",salary);

                            arrayBuilder.add(objectBuilder1.build());



                            //Convert one record for json
//                    String customer = "{\"id\":\"" + id + "\",\"name\":\"" + name + "\",\"address\":\"" + address + "\",\"salary\":" + salary + "},";
//                    allRecords = allRecords + customer;
                        }



                        //Output of allRecords for now
                        //{id:C001,name:Dasun,address:Galle,salary:1000},{id:C001,name:Dasun,address:Galle,salary:1000},

                        //How it should be formatted
                        //[{id:C001,name:Dasun,address:Galle,salary:1000},{id:C001,name:Dasun,address:Galle,salary:1000}]

                        //After last customer object, ',' should be removed
                        // String finalJson = "[" + allRecords.substring(0, allRecords.length() - 1) + "]";

                        //Then print it as the response
                        PrintWriter writer1 = resp.getWriter();

                        //Generate a custom response with json
                        JsonObjectBuilder response1=Json.createObjectBuilder();

                        response1.add("status",200);
                        response1.add("message","Done");
                        response1.add("data",arrayBuilder.build());

                        writer1.print(response1.build());


                        //  writer.write(finalJson); //Possible response types -> //text //xml //html //json

                        break;
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        }

        //This method can be used to save a customer
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            //read json array from a request
//            JsonReader reader = Json.createReader(req.getReader());
//            JsonArray jsonArray = reader.readArray();
//            for (JsonValue jsonValue : jsonArray) {
//
//                String customerID = jsonValue.asJsonObject().getString("id");
//                String customerName = jsonValue.asJsonObject().getString("name");
//                String customerAddress = jsonValue.asJsonObject().getString("address");
//                String customerSalary = jsonValue.asJsonObject().getString("salary");
//                System.out.println(customerID + " " + customerName + " " + customerAddress + " " + customerSalary);
//            }
            String customerID = req.getParameter("CustomerID"); // name value from the input field
            String customerName = req.getParameter("CustomerName");
            String customerAddress = req.getParameter("CustomerAddress");
            String salary = req.getParameter("CustomerSalary");
//            System.out.println(customerID + " " + customerName + " " + customerAddress + " " + salary);
            PrintWriter writer = resp.getWriter();
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "1234");

                PreparedStatement pstm = connection.prepareStatement("Insert into Customer values(?,?,?,?)");
                pstm.setObject(1, customerID);
                pstm.setObject(2, customerName);
                pstm.setObject(3, customerAddress);
                pstm.setObject(4, salary);
                boolean b = pstm.executeUpdate() > 0;


                if (b) {
                    //writer.write("Customer Added");
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    resp.setStatus(HttpServletResponse.SC_OK);//200
                    response.add("status", 200);
                    response.add("message", "Successfully Added");
                    response.add("data", "");
                    writer.print(response.build());
                }

            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//                resp.sendError(500, e.getMessage());
                  resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "Error");
                response.add("data", e.getLocalizedMessage());
                writer.print(response.build());

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); //400
                e.printStackTrace();
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//                resp.sendError(500, throwables.getMessage());
                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 400);
                response.add("message", "Error");
                response.add("data", throwables.getLocalizedMessage());
                writer.print(response.build());

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); //400
                throwables.printStackTrace();
            }
        }
//
        //This method can be used to delete a customer.
        @Override
        protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            //if we send data from the application/x-www-form-urlencoded type, doDelete will not
            //catch values from req.getParameter(); that type is not supported with doDelete
            //but we can send data via Query String
            resp.setContentType("application/json");
            String customerID = req.getParameter("CustomerID");
            PrintWriter writer = resp.getWriter();

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "1234");

                PreparedStatement pstm = connection.prepareStatement("Delete from customer where id=?");
                pstm.setObject(1, customerID);


                if (pstm.executeUpdate() > 0) {

                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("status",200);
                    objectBuilder.add("message","Successfully Deleted");
                    objectBuilder.add("data","");
                    writer.print(objectBuilder.build());
                }else{
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("status",400);
                    objectBuilder.add("data","Wrong Id Inserted");
                    objectBuilder.add("message","");
                    writer.print(objectBuilder.build());
                }


                //            {
//                "data": "",
//                "message":"Succsefully Deleted",
//                "status":"200"
//            }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
//                resp.sendError(500, e.getMessage());
                resp.setStatus(HttpServletResponse.SC_OK);
               JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
               jsonObjectBuilder.add("status",500);
               jsonObjectBuilder.add("message",e.getLocalizedMessage());
               jsonObjectBuilder.add("data","");
               writer.print(jsonObjectBuilder.build());

            } catch (SQLException throwables) {
                throwables.printStackTrace();
//                resp.sendError(500, throwables.getMessage());
                resp.setStatus(HttpServletResponse.SC_OK);
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                jsonObjectBuilder.add("status",500);
                jsonObjectBuilder.add("message",throwables.getLocalizedMessage());
                jsonObjectBuilder.add("data","");
                writer.print(jsonObjectBuilder.build());
            }

        }

        //This method can be used to update a customer
        @Override
        protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//            String customerID = req.getParameter("customerID"); // name value from the input field
//            String customerName = req.getParameter("customerName");
//            String customerAddress = req.getParameter("customerAddress");
//            String customerSalary = req.getParameter("customerSalary");

            resp.setContentType("application/json");
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String address = jsonObject.getString("address");
            String salary = jsonObject.getString("salary");

            PrintWriter writer = resp.getWriter();

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "1234");

                PreparedStatement pstm = connection.prepareStatement("Update customer set name=?,address=?,salary=? where id=?");
                pstm.setObject(1, name);
                pstm.setObject(2, address);
                pstm.setObject(3, salary);
                pstm.setObject(4, id);
                boolean b = pstm.executeUpdate() > 0;

                if (b) {
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("status", 200);
                    objectBuilder.add("message", "Successfully Updated");
                    objectBuilder.add("data", "");
                    writer.print(objectBuilder.build());
                } else {
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    objectBuilder.add("status", 400);
                    objectBuilder.add("message", "Update Failed");
                    objectBuilder.add("data", "");
                    writer.print(objectBuilder.build());
                }
                //            {
//                "data": "",
//                "message":"Succsefully Updated",
//                "status":"200"
//            }

            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//                resp.sendError(500, e.getMessage());
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 500);
                objectBuilder.add("message", "Update Failed");
                objectBuilder.add("data", e.getLocalizedMessage());
                writer.print(objectBuilder.build());
            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//                resp.sendError(500, throwables.getMessage());
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 500);
                objectBuilder.add("message", "Update Failed");
                objectBuilder.add("data", throwables.getLocalizedMessage());
                writer.print(objectBuilder.build());
            }
        }
  }

