package dedep.serv;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HelloServlet extends HttpServlet {

    public static final String DATABASE_URL = "http://localhost:5984/werber/";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        String docId = request.getParameter("doc");
        String att = request.getParameter("att");

        if (docId == null || att == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        ServletOutputStream out;
        try {
            out = response.getOutputStream();
            URL url = new URL(DATABASE_URL + docId + "/" + att);
            response.setContentType(getImageContentType(url));
            streamImage(url, out);

        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private String getImageContentType(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        connection.connect();
        return connection.getContentType();
    }

    private void streamImage(URL url, ServletOutputStream out) throws IOException {
        InputStream fin = url.openStream();
        BufferedInputStream bin = new BufferedInputStream(fin);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        int ch = 0;
        while ((ch = bin.read()) != -1) {
            bout.write(ch);
        }

        bin.close();
        fin.close();
        bout.close();
        out.close();
    }
}
