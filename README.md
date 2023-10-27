# Full Stack Assignment - Books Database


## Table of Contents

1. [About](#about)
2. [Problem Statement](#problem-statement)
3. [Project Setup](#project-setup)
4. [Project Structure](#project-structure)
5. [How to Run](#how-to-run)
6. [Sample Outputs](#sample-outputs)
7. [Acknowledgements](#acknowledgments)

## About

This project was set as a final first year Full Stack development course assignment. It involves creating a 3-tier TCP-based networking multi-threaded client-server application to consult a database about books. The application features a client with a JavaFX-based graphical user interface that communicates with an intermediate server providing a query-specific service. The server connects to a PostgreSQL database using JDBC to retrieve information about books based on user queries.

## Problem Statement

The project aims to integrate various components, including database access, server-side processing, and a graphical user interface. The main tasks include implementing a server, a client, and connecting to a PostgreSQL database to retrieve book information based on user queries.

## Project Setup

Before starting, make sure to set up the database and import the provided `books.sql` file into your PostgreSQL database.
You will need to download PostgreSQL for this to work.

## Project Structure

The project contains the following Java files:
- `BooksDatabaseServer.java`: The server's main class.
- `BooksDatabaseService.java`: The service provider responsible for handling client requests.
- `BooksDatabaseClient.java`: The client application with a JavaFX GUI.

## How to Run

To run the project, follow these steps:
1. Set up the database by importing the `books.sql` file into your PostgreSQL database. To do this, open your PSQL shell, connect to the default port, enter your username and password. Then run the command:
` \i 'C:/Users/YourUsername/Downloads/books.sql' `
changing as necessary to where you saved the books.sql
2. in the Credentials.java class, change the `USERNAME` and `PASSWORD` to the one you used in PSQL.
3. Compile the Java files.
4. Run the `BooksDatabaseServer.java` to start the server.
5. Run the `BooksDatabaseClient.java` to start the client application.

## Sample Outputs

### Server Output

The server's output will be displayed in the console. It includes information about incoming service requests and the corresponding thread IDs.

### Client Output

The client application provides both GUI and console output. You can see the service requests and their outcomes in the console. The GUI displays the results in a table format.

## Acknowledgments

This project was completed as part of an assignment for University Of Birmingham under the guidance of Felipe Orihuela-Espina. The project templates and guidelines were provided by the university, and I appreciate their valuable support throughout the assignment.



