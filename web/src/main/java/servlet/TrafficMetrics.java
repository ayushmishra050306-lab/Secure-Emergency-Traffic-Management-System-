package servlet;

import ejb.remote.AnalyticalServer;
import jakarta.ejb.EJB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "trafficMetrics", urlPatterns = "/traffic-metrics")
public class TrafficMetrics extends HttpServlet {
    @EJB
    AnalyticalServer analyticalServer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(analyticalServer.identifyTrafficPatterns());

        if (analyticalServer.identifyTrafficPatterns().isEmpty()) {
            resp.getWriter().write("No Data Available");
        } else {

            double v = analyticalServer.calculateAverageVehicleSpeed();
            Map<LocalDateTime, Double> averageVehicleSpeedMap = analyticalServer.identifyTrafficPatterns();
            Set<LocalDateTime> averageVehicleSpeedMapKeys = averageVehicleSpeedMap.keySet();


            Map<LocalDateTime, Double> trafficDensityMap = analyticalServer.trafficDensity();
            Set<LocalDateTime> trafficDensityMapKeys = trafficDensityMap.keySet();

            List<Map<LocalDateTime, Double>> mobilityList = analyticalServer.mobilityOfTheArea();
            Map<LocalDateTime, Double> ttiMap = mobilityList.get(0);
            Set<LocalDateTime> ttiMapKeys = ttiMap.keySet();
            Map<LocalDateTime, Double> varianceTtiMap = mobilityList.get(1);
            Set<LocalDateTime> varianceTtiMapKeys = varianceTtiMap.keySet();
            Map<LocalDateTime, Double> standardDevTtiMap = mobilityList.get(2);
            Set<LocalDateTime> standardDevTtiMapKeys = standardDevTtiMap.keySet();

            req.setAttribute("averageVehicleSpeedMapKeys", averageVehicleSpeedMapKeys);
            req.setAttribute("averageVehicleSpeedMap", averageVehicleSpeedMap);
            req.setAttribute("trafficDensityMapKeys", trafficDensityMapKeys);
            req.setAttribute("trafficDensityMap", trafficDensityMap);
            req.setAttribute("ttiMapKeys", ttiMapKeys);
            req.setAttribute("ttiMap", ttiMap);
            req.setAttribute("varianceTtiMapKeys", varianceTtiMapKeys);
            req.setAttribute("varianceTtiMap", varianceTtiMap);
            req.setAttribute("standardDevTtiMapKeys", standardDevTtiMapKeys);
            req.setAttribute("standardDevTtiMap", standardDevTtiMap);

            RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
