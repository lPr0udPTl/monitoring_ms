package hello.queue;

import com.netflix.client.IResponse;
import com.netflix.zuul.context.RequestContext;
import hello.database.DatabaseConnection;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class RequestQueue {
    BlockingQueue<RequestContext> requestQueue;
    DatabaseConnection db;
    Connection conn;

    public RequestQueue() {
        requestQueue = new LinkedBlockingDeque<>();
        db = new DatabaseConnection();
        conn = db.connect();
        db.createTable(conn);
    }

    public void add(RequestContext element) {
        requestQueue.add(element);
    }

    public void start() {
        Thread thread = new Thread(new Runnable() {
            public void run()
            {
                try{
                    while (true) {
                        RequestContext element = requestQueue.take();
                        processRequest(element);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }


            }});
        thread.start();
    }

    public void processRequest(RequestContext ctx) {
        HttpServletRequest request = ctx.getRequest();

        System.out.println("HERE!!!!!");
        System.out.println(request.getRequestURL().toString());

        /*String timeStart = ctx.getZuulRequestHeaders().get("time-start");
        String timeEnd = ctx.getZuulRequestHeaders().get("time-end");
        String sourceIp = request.getRemoteAddr();
        int sourcePort = request.getRemotePort();
        String destinyMicroservice = request.getRequestURL().toString().split("/")[3];
        String instance = ((IResponse) ctx.get("ribbonResponse")).getRequestedURI().toString().substring(7);
        String destinyInstance = instance.split("/")[0];
        String path = null;
        if(instance.indexOf('/') == -1) {
            path = "/";
        }
        else {
            path = instance.substring(instance.indexOf("/")+1).trim();
        }
        String destinyFunction = request.getMethod() + " -> " + path;

        long tStart = Long.parseLong(timeStart);
        long tEnd = Long.parseLong(timeEnd);


        String destinyIp = null;

        try{
            // Get IP given instance name
            InetAddress addr = InetAddress.getByName(destinyInstance.split(":")[0]);
            destinyIp = addr.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }


        db.addEntry(conn, tStart, tEnd, tEnd - tStart, sourceIp, sourcePort, destinyMicroservice, destinyInstance, destinyIp, destinyFunction);*/
    }
}
