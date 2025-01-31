// Import the express and url modules
const express = require('express');
const url = require("url");

// Status codes defined in external file
require('./http_status.js');

// The express module is a function. When executed, it returns an app object
const app = express();

// Import the mysql module
const mysql = require('mysql');

// Create a connection object with the user details
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "",
    database: "coursework_1",
    debug: false,
    port: "3308"
});

// Set up the application to handle GET requests sent to the user path
app.get('/watch/*', handleGetRequest); // Subfolders
app.get('/watch', handleGetRequest);

// Serve up static pages from the public folder
app.use(express.static('public'));

// Start the app listening on port 8080
app.listen(8080, () => {
    console.log('Server is running on port 8080');
});

app.get('/watch/*'); // Subfolders
app.get('/watch');

module.exports = app;

/* Handles GET requests sent to the web service.
   Processes path and query string and calls appropriate functions to
   return the data. */
function handleGetRequest(request, response) {
    // Parse the URL
    const urlObj = url.parse(request.url, true);

    // Extract object containing queries from URL object.
    const queries = urlObj.query;

    // Get the pagination properties if they have been set. Will be undefined if not set.
    const numItems = queries['num_items'];
    const offset = queries['offset'];

    // Split the path of the request into its components
    const pathArray = urlObj.pathname.split("/");

    // Get the last part of the path
    const pathEnd = pathArray[pathArray.length - 1];

    const description = pathArray[pathArray.length - 2];

    // If path ends with 'watch', return all watches
    if (pathEnd === 'watch') {
        getTotalWatchesCount(response, numItems, offset); // This function calls the getAllWatches function in its callback
        return;
    }

    // If path ends with 'search', return watch details by description
    if (pathEnd === 'search') {
        getWatchDetailsByDescription(response, description); // This function calls the getAllWatches function in its callback
        return;
    }

    // If path ends with 'watch/', return all watches
    if (pathEnd === '' && pathArray[pathArray.length - 2] === 'watch') {
        getTotalWatchesCount(response, numItems, offset); // This function calls the getAllWatches function in its callback
        return;
    }

    // If the last part of the path is a valid watch id, return data about that watch
    const regEx = new RegExp('^[0-9]+$'); // RegEx returns true if the string is all digits.
    if (regEx.test(pathEnd)) {
        getWatchDetailsById(response, pathEnd);
        return;
    }

    // The path is not recognized. Return an error message
    response.status(HTTP_STATUS.NOT_FOUND);
    response.send("{error: 'Path not recognized', url: " + request.url + "}");
}

/** Returns all of the watches, possibly with a limit on the total number of items returned and the offset (to
 *  enable pagination). This function should be called in the callback of getTotalWatchesCount  */
function getAllWatches(response, totNumItems, numItems, offset) {
    // Select the watches data using JOIN to convert foreign keys into useful data.
    let sql = "SELECT watch.watch_id, watch.prod_name, watch.brand, watch.description, watch.image_url FROM watch ";

    // Limit the number of results returned if this has been specified in the query string
    if (numItems !== undefined && offset !== undefined) {
        sql += "ORDER BY watch.watch_id LIMIT " + numItems + " OFFSET " + offset;
    }

    // Execute the query
    connectionPool.query(sql, function (err, result) {

        // Check for errors
        if (err) {
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        // Create a JavaScript object that combines the total number of items with data
        const returnObj = {totNumItems: totNumItems};
        returnObj.data = result; // Array of data from the database

        // Return results in JSON format
        response.json(returnObj);
    });
}

/** When retrieving all watches, we start by retrieving the total number of watches.
 * The database callback function will then call the function to get the watch data with pagination. */
function getTotalWatchesCount(response, numItems, offset) {
    const sql = "SELECT COUNT(*) FROM watch";

    // Execute the query and call the anonymous callback function.
    connectionPool.query(sql, function (err, result) {

        // Check for errors
        if (err) {
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        // Get the total number of items from the result
        const totNumItems = result[0]['COUNT(*)'];

        // Call the function that retrieves all watches
        getAllWatches(response, totNumItems, numItems, offset);
    });
}

function getWatchDetailsById(response, id) {
    // Build SQL query to select watch data with the specified id.
    const sql = " SELECT watch.brand, watch.image_url, watch.description, comparison.url, comparison.price, comparison.website FROM comparison INNER JOIN watch ON comparison.watch_id = watch.watch_id WHERE watch.watch_id = " + id ;

    // Execute the query
    connectionPool.query(sql, function (err, result) {
        // Check for errors
        if (err) {
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({ 'error': true, 'message': + err });
            return;
        }

        // Output results in JSON format
        response.json(result);
    });
}

function getWatchDetailsByDescription(response, description) {
    // Build SQL query to select watch data with the specified id.
    // Select the watches data using JOIN to convert foreign keys into useful data.
    const searchTerm = description;
    // Construct the SQL query with a parameterized query
    const sql = "SELECT watch.watch_id, watch.prod_name, watch.brand, watch.description, watch.image_url FROM watch WHERE watch.description LIKE ?";

    connectionPool.query(sql, ['%' + searchTerm + '%'], function (err, result) {
        // Check for errors
        if (err) {
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({ 'error': true, 'message': + err });
            return;
        }

        // Output results in JSON format
        response.json(result);
    });
}
