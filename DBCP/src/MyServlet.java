import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/hello")
public class MyServlet extends HttpServlet {
    public MyServlet() {
        System.out.println("Onna object ekak haduna");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Onna mama Servlet ekak una");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Onna Get Request ekak labuna");
    }

    @Override
    public void destroy() {
        System.out.println("Onna mama mala");
    }
}
