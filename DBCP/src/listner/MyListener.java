package listner;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;

public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Context Initialized");

        //How to create the DBCP pool
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/thogakade");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("1234");
        basicDataSource.setMaxTotal(5);// how many connections
        basicDataSource.setInitialSize(5);// how many connection we should initialize

        ServletContext servletContext = servletContextEvent.getServletContext();// a common place for all servlet
        servletContext.setAttribute("basicDataSource",basicDataSource);// store the pool inside the Servlet Context

        System.out.println("Context Initialized");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Context Destroyed");
        ServletContext servletContext = servletContextEvent.getServletContext();
        BasicDataSource bds = (BasicDataSource) servletContext.getAttribute("basicDataSource");
        try {
            bds.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
