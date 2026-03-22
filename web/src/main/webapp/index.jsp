<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="ejb.remote.AnalyticalServer" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="averageVehicleSpeedMap" scope="request" type="java.util.LinkedHashMap"/>
<jsp:useBean id="averageVehicleSpeedMapKeys" scope="request" type="java.util.Set"/>
<jsp:useBean id="trafficDensityMap" scope="request" type="java.util.LinkedHashMap"/>
<jsp:useBean id="trafficDensityMapKeys" scope="request" type="java.util.Set"/>
<jsp:useBean id="ttiMap" scope="request" type="java.util.LinkedHashMap"/>
<jsp:useBean id="ttiMapKeys" scope="request" type="java.util.Set"/>
<jsp:useBean id="varianceTtiMap" scope="request" type="java.util.LinkedHashMap"/>
<jsp:useBean id="varianceTtiMapKeys" scope="request" type="java.util.Set"/>
<jsp:useBean id="standardDevTtiMap" scope="request" type="java.util.LinkedHashMap"/>
<jsp:useBean id="standardDevTtiMapKeys" scope="request" type="java.util.Set"/>

<html>
<head>
    <title>Traffic Metrics</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            border: 1px solid #000;
            padding: 8px;
        }

        th {
            background-color: #f2f2f2;
            text-align: left;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
    </style>
    <script>
        function showPage(pageNum) {
            var rows = document.querySelectorAll("#avgSpeedTable tbody tr");
            var start = (pageNum - 1) * 10;
            var end = start + 10;

            for (var i = 0; i < rows.length; i++) {
                if (i >= start && i < end) {
                    rows[i].style.display = "table-row";
                } else {
                    rows[i].style.display = "none";
                }
            }
        }

        function showTrafficDensityTablePage(pageNum) {
            var rows = document.querySelectorAll("#trafficDensityTable tbody tr");
            var start = (pageNum - 1) * 10;
            var end = start + 10;

            for (var i = 0; i < rows.length; i++) {
                if (i >= start && i < end) {
                    rows[i].style.display = "table-row";
                } else {
                    rows[i].style.display = "none";
                }
            }
        }

        function showMobilityTableTablePage(pageNum) {
            var rows = document.querySelectorAll("#mobilityTable tbody tr");
            var start = (pageNum - 1) * 10;
            var end = start + 10;

            for (var i = 0; i < rows.length; i++) {
                if (i >= start && i < end) {
                    rows[i].style.display = "table-row";
                } else {
                    rows[i].style.display = "none";
                }
            }
        }

        function createPaginationButtons(totalPages) {
            var avgSpeedPaginationDiv = document.getElementById("avgSpeedPagination");

            for (var i = 1; i <= totalPages; i++) {
                var button = document.createElement("button");
                button.textContent = i;
                button.onclick = function () {
                    showPage(this.textContent);
                };
                avgSpeedPaginationDiv.appendChild(button);
            }
        }

        function createTrafficDensityTablePaginationButtons(totalPages) {
            var trafficFlowPaginationDiv = document.getElementById("trafficFlowPagination");

            for (var i = 1; i <= totalPages; i++) {
                var button = document.createElement("button");
                button.textContent = i;
                button.onclick = function () {
                    showTrafficDensityTablePage(this.textContent);
                };
                trafficFlowPaginationDiv.appendChild(button);
            }
        }

        function createMobilityTablePaginationButtons(totalPages) {
            var mobilityPaginationDiv = document.getElementById("mobilityPagination");

            for (var i = 1; i <= totalPages; i++) {
                var button = document.createElement("button");
                button.textContent = i;
                button.onclick = function () {
                    showMobilityTableTablePage(this.textContent);
                };
                mobilityPaginationDiv.appendChild(button);
            }
        }

        window.onload = function () {
            var totalRows = document.querySelectorAll("#avgSpeedTable tbody tr").length;
            var totalPages = Math.ceil(totalRows / 10);
            createPaginationButtons(totalPages);
            showPage(1);

            var totalRowsForTrafficDensityTable = document.querySelectorAll("#trafficDensityTable tbody tr").length;
            var trafficDensityTableTotalPages = Math.ceil(totalRowsForTrafficDensityTable / 10);
            createTrafficDensityTablePaginationButtons(trafficDensityTableTotalPages);
            showTrafficDensityTablePage(1);

            var totalRowsForMobilityTable = document.querySelectorAll("#mobilityTable tbody tr").length;
            var mobilityTableTotalPages = Math.ceil(totalRowsForMobilityTable / 10);
            createMobilityTablePaginationButtons(mobilityTableTotalPages);
            showMobilityTableTablePage(1);
        };
    </script>
</head>
<body>
<h1>Traffic Metrics</h1>

<h3>Average Speed For Time Intervals</h3>
<table id="avgSpeedTable">
    <thead>
    <tr>
        <th>Time</th>
        <th>Average Speed (KMPH)</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="entry" items="${averageVehicleSpeedMapKeys}">
        <tr>
            <td>${entry}</td>
            <td>${averageVehicleSpeedMap.get(entry)}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div id="avgSpeedPagination"></div>

<h3>Traffic Density For Time Intervals</h3>
<table id="trafficDensityTable">
    <thead>
    <tr>
        <th>Time</th>
        <th>Traffic Flow</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="entry" items="${trafficDensityMapKeys}">
        <tr>
            <td>${entry}</td>
            <td>${trafficDensityMap.get(entry)}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div id="trafficFlowPagination"></div>
<canvas id="trafficFlowChart" width="800" height="400"></canvas>

<h3>Mobility For Time Intervals</h3>
<table id="mobilityTable">
    <thead>
    <tr>
        <th>Time</th>
        <th>Average Time Travel Index</th>
        <th>Variance Of TTI</th>
        <th>Standard Deviation of TTI</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="entry" items="${ttiMapKeys}">
        <tr>
            <td>${entry}</td>
            <td>${ttiMap.get(entry)}</td>
            <td>${varianceTtiMap.get(entry)}</td>
            <td>${standardDevTtiMap.get(entry)}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div id="mobilityPagination"></div>

<canvas id="lineChart" width="800" height="400"></canvas>


<script src="chart.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/chartjs-chart-box-and-violin-plot/4.0.0/Chart.BoxPlot.min.js" integrity="sha512-z2lEnxnLHbu/Vp4PxvIyCCy/QX1mmq2ocpgqTf50xeSDn35VTra3gUi7Qs53qSLj99aJ9SMtqP08UFt+lJmHxA==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script>
    <%
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    Set<String> stringSet=new LinkedHashSet<>();
    StringBuilder keysString=null;
    for (Object key : ttiMapKeys) {
        LocalDateTime dateTime = LocalDateTime.parse(key.toString());
        String formattedKeyString = formatter.format(dateTime);
        keysString = new StringBuilder();
        keysString.append("'").append(formattedKeyString).append("'");

        stringSet.add(keysString.toString());
    }

    pageContext.setAttribute("ttiMapKeys", stringSet);
    System.out.println("Keys As String..."+stringSet);


%>

    var ttiMapKeys = ${ttiMapKeys};
    var ttiMapValues = ${ttiMap.values()};
    var ttiVarianceMapKeys = ${ttiMapKeys};
    var ttiVarianceMapValues = ${varianceTtiMap.values()};
    var ttiSDMapKeys = ${ttiMapKeys};
    var ttiSDMapValues = ${standardDevTtiMap.values()};

    var traffiFlowValues = ${trafficDensityMap.values()};

    const labels = ttiMapKeys;
    console.log(labels);
    const data = {
        labels: labels,
        datasets: [{
            label: 'Traffic Flow',
            data: traffiFlowValues,
            fill: false,
            borderColor: 'rgb(75, 192, 192)',
            borderWidth: 2,
            pointRadius: 5,
            pointHoverRadius: 7,
            tension: 0.1
        }]
    };

    var ctx = document.getElementById('trafficFlowChart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            title: {
                display: true,
                text: 'Traffic Flow Line Chart'
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Time'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Traffic Flow'
                    }
                },
            },
        }
    });



    var ctx2 = document.getElementById('lineChart').getContext('2d');
    new Chart(ctx2, {
        type: 'bar',
        data: {
            labels: ttiMapKeys,
            datasets: [
                {
                    label: 'TTI',
                    data: ttiMapValues,
                    borderColor: 'blue',
                    backgroundColor: 'blue',
                    type: 'line',
                    yAxisID: 'tti-y-axis'
                },
                {
                    label: 'Variance',
                    data: ttiVarianceMapValues,
                    borderColor: 'green',
                    backgroundColor: 'green',
                    yAxisID: 'variance-y-axis'
                },
                {
                    label: 'Standard Deviation',
                    data: ttiSDMapValues,
                    borderColor: 'brown',
                    backgroundColor: 'brown',
                    yAxisID: 'std-deviation-y-axis'
                }
            ]
        },
        options: {
            scales: {
                yAxes: [
                    {
                        id: 'tti-y-axis',
                        type: 'linear',
                        position: 'left',
                        scaleLabel: {
                            display: true,
                            labelString: 'TTI'
                        }
                    },
                    {
                        id: 'variance-y-axis',
                        type: 'linear',
                        position: 'right',
                        scaleLabel: {
                            display: true,
                            labelString: 'Variance'
                        }
                    },
                    {
                        id: 'std-deviation-y-axis',
                        type: 'linear',
                        position: 'right',
                        scaleLabel: {
                            display: true,
                            labelString: 'Standard Deviation'
                        },
                        gridLines: {
                            display: false
                        }
                    }
                ]
            }
        }
    });
</script>

</body>
</html>
