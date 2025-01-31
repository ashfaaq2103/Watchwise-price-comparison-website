// Function to handle the search button click
function clicked() {
    // Get the search value from the input field
    let searchValue = document.getElementById("search_btn").value;

    // Make an AJAX request to fetch watches based on the search value
    $.ajax({
        url: `/watch/${searchValue}/search`,
        method: 'GET',
        success: function (data) {
            // Check if the 'app' variable is defined (Vue instance)
            if (typeof app !== 'undefined') {
                // Clear existing watches in the Vue instance
                app.watch = [];

                // Add new watches from the search result to the Vue instance
                data.forEach(item => {
                    app.watch.push(item);
                });

                // Clear pagination
                document.getElementById('pagination').innerHTML = '';

                // Check if the data array is empty
                if (data.length === 0) {
                    // Display a message if no results found
                    var watchContainer = document.getElementById('watchContainer');
                    watchContainer.innerHTML = '<p class="watchContainers"> No results for your search </p>';

                    // Clear pagination
                    document.getElementById('pagination').innerHTML = '';
                }
            } else {
                // Redirect to products.html with the search query as a parameter
                window.location.href = 'products.html?search=' + encodeURIComponent(searchValue);
            }
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function clicked1()
{
    // Get the search value from the input field
    let searchValue = document.getElementById("search_btn1").value;

    // Make an AJAX request to fetch watches based on the search value
    $.ajax({
        url: `/watch/${searchValue}/search`,
        method: 'GET',
        success: function (data) {
            // Redirect to products.html with the search query as a parameter
            window.location.href = 'products.html?search=' + encodeURIComponent(searchValue);
        },
        error: function (error) {
            console.error(error);
        }
    });
}

// Function to load watches based on the search value
function loadWatchesBySearch(searchValue) {
    // Make an AJAX request to fetch watches based on the search value
    $.ajax({
        url: `/watch/${searchValue}/search`,
        method: 'GET',
        success: function (data) {
            // Clear existing watches in the Vue instance
            app.watch = [];

            // Add new watches from the search result to the Vue instance
            console.log(data);
            if (data.length === 0) {
                // Display a message if no results found
                var watchContainer = document.getElementById('watchContainer');
                watchContainer.innerHTML = '<p class="watchContainers"> No results for your search </p>';

                // Clear pagination
                document.getElementById('pagination').innerHTML = '';
            }

            data.forEach(item => {
                app.watch.push(item);
            });

            // Clear pagination
            document.getElementById('pagination').innerHTML = '';
        },
        error: function (error) {
            console.error(error);
        }
    });
}

// Function to parse query parameters from URL
function getQueryParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

// Event listener when the DOM is fully loaded
document.addEventListener('DOMContentLoaded', function () {
    // Get the search query from the URL
    var searchQuery = getQueryParam('search');

    if (searchQuery) {
        // Load watches by search when a search query is present in the URL
        loadWatchesBySearch(searchQuery);
    }
});
