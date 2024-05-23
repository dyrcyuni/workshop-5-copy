package app;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for using JDBC to talk to a SQLLite (MySQL) Database.
 * Allows SQL queries to be used with the Databse in Java.
 *
 * 🚨 THIS VERSION OF THE JDBC FILE USES ARRAYLIST of OBJECTS!
 * If you are not yet ready to work with Java Objects, then use the
 * JDBCStrings.java version
 * 
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class JDBCConnection {

    // Name of database file (contained in database folder)
    public static final String DATABASE = "jdbc:sqlite:database/Movies.db";

    /**
     * This creates a JDBC Object so we can keep talking to the database
     */
    public JDBCConnection() {
        System.out.println("Created JDBCObjects Object to work with an ArrayList of Objects");
    }

    /**
     * Get all of the Movie Titles in the database
     */
    public ArrayList<String> getMovieTitles() {
        // Create the ArrayList to return - of Strings for the movie titles
        ArrayList<String> movies = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM movie";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String movieName = results.getString("mvtitle");

                // Store the movieName in the ArrayList to return
                movies.add(movieName);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movie titles
        return movies;
    }

    /**
     * Get all of the Movies in the database.
     * 
     * @return
     *         Returns an ArrayList of Movie objects
     */
    public ArrayList<Movie> getMovies() {
        // Create the ArrayList to return - this time of Movie objects
        ArrayList<Movie> movies = new ArrayList<Movie>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM movie";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // Create a Movie Object
                Movie movie = new Movie();

                // Lookup the columns we want, and set the movie object field
                // BUT, we must be careful of the column type!
                movie.id = results.getInt("mvnumb");
                movie.name = results.getString("mvtitle");
                movie.year = results.getInt("yrmde");
                movie.genre = results.getString("mvtype");

                // Add the movie object to the array
                movies.add(movie);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return movies;
    }

    public int countMovies() {
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT COUNT(*) as Count FROM Movie";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                return results.getInt("Count");
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return 0;
    }

    public ArrayList<Movie> getMoviesByType(String movieType) {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Movie WHERE Movie.MvType = '" + movieType + "'";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                Movie movie = new Movie();
                movie.id = results.getInt("mvnumb");
                movie.name = results.getString("mvtitle");
                movie.year = results.getInt("yrmde");
                movie.genre = results.getString("mvtype");
                movies.add(movie);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return movies;
    }

    public ArrayList<Movie> getMoviesWithNominationsByTypeOrMoviesByType(String movieTypeA, String movieTypeB) {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Movie"
                    + " WHERE (Movie.MvType = '" + movieTypeA + "' AND Movie.Noms > 0)"
                    + " OR (Movie.MvType = '" + movieTypeB + "')";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                Movie movie = new Movie();
                movie.id = results.getInt("mvnumb");
                movie.name = results.getString("mvtitle");
                movie.year = results.getInt("yrmde");
                movie.genre = results.getString("mvtype");
                movies.add(movie);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return movies;
    }

    public ArrayList<Movie> getMoviesWithAwards(int minAwards) {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Movie WHERE Movie.Awrd >= " + minAwards;

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                Movie movie = new Movie();
                movie.id = results.getInt("mvnumb");
                movie.name = results.getString("mvtitle");
                movie.year = results.getInt("yrmde");
                movie.genre = results.getString("mvtype");
                movies.add(movie);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return movies;
    }

    public Movie getMostRecentMovie() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Movie ORDER BY YrMde DESC LIMIT 1";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                Movie movie = new Movie();
                movie.id = results.getInt("mvnumb");
                movie.name = results.getString("mvtitle");
                movie.year = results.getInt("yrmde");
                movie.genre = results.getString("mvtype");
                return movie;
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return null;
    }
}
