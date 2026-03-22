# Smart Urban Traffic Management System (SUTMS)

## Introduction
The Smart Urban Traffic Management System (SUTMS) is designed to manage urban traffic efficiently by simulating IoT device data and processing it to output meaningful traffic metrics.

## Project Overview
Smart Urban Traffic Management System (SUTMS) is a comprehensive solution that includes a Standard Edition (SE) application for simulating IoT device data, a central server for data processing, and a presentation layer for displaying traffic metrics. The system leverages Enterprise JavaBeans (EJB) for server-side components and is deployed as a Maven multi-module project.

## Features
- **IoT Device Simulation**: Simulates data from IoT devices in urban traffic scenarios.
- **Central Server Processing**: Processes simulated data to manage traffic density and vehicle speed.
- **Traffic Metrics Visualization**: Presents analyzed traffic data to clients through web interfaces.

## Installation
To install SUTMS, clone the repository and navigate to the project directory:
1. Clone the repository to your local machine.
2. Open the project.
3. Place the database file traffic_data.db [https://drive.google.com/file/d/1hOeimuWhOa2dpnEd3ASeOwC-9w7-n_hz/view?usp=drive_link] in this location to view the metrices in the jsp --> C:\payara6\glassfish\domains\domain1\config
4. Build the project and deploay EAR artifact in a application server ex:- payara/glassfish.
5. Serach for this url-->http://localhost:8080/web/traffic-metrics

## Technologies Used
- **Java SE**: For simulating IoT devices.
- **EJB**: For encapsulating business logic in server-side components.
- **Maven**: For project management and build automation.
- **Payara Server**: For hosting the central server application.
- **SQLite**: For database storage and retrieval.
- **JMS**: For communication between the SE application and the central server.

## Architectural Design:

- **The system is structured into four layers**: Presentation, Application, Persistence, and Infrastructure.
- **Presentation Layer**: Comprises web modules with Servlets and JSPs to display traffic metrics to clients1.
- **Application Layer**: Includes a Maven SE application for IoT device simulation and a Maven multi-module project for the central server2.
- **Persistence Layer**: Utilizes SQLite for data storage and retrieval3.
- **Infrastructure Layer**: Employs Payara Server for hosting and JMS for communication between applications.

## EJB Components:
- **Stateless Components**: Such as IOTDeviceBean for handling multiple device requests and TrafficDataDAOBean for independent CRUD operations4.
- **Singleton Components**: Like TrafficEnvironmentBean for managing traffic density and AnalyticalServerBean for consistent data analysis.
- **Message-Driven Bean**:TrafficDataReceiverBean listens for messages from IoT devices and saves them to the database5.

## Functional Design:
Focuses on capturing realistic IoT device data, managing large volumes of data, processing data quality, and visualizing analyzed data for clients.

## Contact
For any inquiries or contributions, please contact Rashmika Jayasooriya at uvindurashmika20010405@outlook.com or call 0766351645 or visit https://rashmikajayasooriya.great-site.net for more information.

## Smart Urban Traffic Management System Traffic Metrics Presentation In Web Images

![Screenshot_2-5-2024_12250_localhost](https://github.com/RashmikaJayasooriya/Smart-Urban-Traffic-Management-System/assets/129141186/bba6f386-a6c6-42be-8101-152ff1055378)

# Thank you for visiting the Smart Urban Traffic Management System repository!
