import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //How to create the DBCP pool
//        BasicDataSource basicDataSource = new BasicDataSource();
//        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        basicDataSource.setUrl("jdbc:mysql://localhost:3306/thogakade");
//        basicDataSource.setUsername("root");
//        basicDataSource.setPassword("1234");
//        basicDataSource.setMaxTotal(5);// how many connections
//        basicDataSource.setInitialSize(5);// how many connection we should initialize
//
//        ServletContext servletContext = req.getServletContext();// a common place for all servlet
//        servletContext.setAttribute("bds",basicDataSource);// store the pool inside the Servlet Context
        ServletContext servletContext = req.getServletContext();
        BasicDataSource basicDataSource = (BasicDataSource) servletContext.getAttribute("basicDataSource");

        try {
            Connection connection = basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customer");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
            String id = resultSet.getString(1);
            System.out.println(id);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        BasicDataSource bds = (BasicDataSource) servletContext.getAttribute("bds");

        try {
            Connection connection = bds.getConnection();
            PreparedStatement pstm = connection.prepareStatement("select * from customer");
            ResultSet rst = pstm.executeQuery();
            while (rst.next()) {
                String id = rst.getString(1);
                System.out.println(id);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
