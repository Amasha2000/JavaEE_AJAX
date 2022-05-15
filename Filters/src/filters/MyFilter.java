package filters;

import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = "/customer")
//@WebFilter(urlPatterns={"/customer","/item","/order"})
//@WebFilter(urlPatterns="/*")
public class MyFilter implements Filter {

        public MyFilter() {
            System.out.println("Object Created from MyFilter");
        }

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            System.out.println("My Filter Initialized");
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            //Before the request send
            System.out.println("First");

          // HttpServletRequest req= (HttpServletRequest) servletRequest;
            HttpServletResponse res= (HttpServletResponse) servletResponse;
            // without this line the request will not proceed to the servlet
            filterChain.doFilter(servletRequest,servletResponse);// proceed request to the servlet

//            PrintWriter writer = servletResponse.getWriter();
//            writer.write("Added from MyFilter");

            res.addHeader("MyCompany", "IJSE");
            res.addHeader("Access-Control-Allow-Origin", "*");
            res.addHeader("Access-Control-Allow-Methods", "DELETE, PUT");
            res.addHeader("Access-Control-Allow-Headers", "Content-Type");

            //After the servlet response
            System.out.println("Second");
        }


        @Override
        public void destroy() {
            System.out.println("Destroy method invoked");
        }
    }

