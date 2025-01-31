// Parse the query parameter to get the watch ID
const urlParams = new URLSearchParams(window.location.search);
const watchId = urlParams.get('watch_id');

// Make an AJAX request to fetch watch data based on the watch ID
$.ajax({
    url: `/watch/${watchId}`, // Replace with the correct server endpoint for fetching watch details
    method: 'GET',
    success: function (data) {
        // Clear existing table rows
        const table = document.querySelector('.responsive-table');
        table.innerHTML = '\n' +
            '            <li class="table-header">\n' +
            '                <div class="col col-1">Website Name</div>\n' +
            '                <div class="col col-2">Price($)</div>\n' +
            '                <div class="col col-4"></div>\n' +
            '            </li>';

        // Iterate over the data and add rows to the table
        data.forEach(item => {
            const brand = item.brand;
            const description = item.description;
            const url = item.url;
            const price = item.price;
            const imageUrl = item.image_url;
            const website = item.website;

            // Add a new row to the table
            const newRow = document.createElement('li');
            newRow.className = 'table-row';
            newRow.innerHTML = `
          <div class="col col-1" >${website}</div>
          <div class="col col-2 price" >$${price}</div>
          <div class="col col-3" >
            <a href="${url}" target="_blank" class="link-button">Visit Website</a>
          </div>
        `;

            table.appendChild(newRow);
        });

        // Update the comparison_page content
        document.querySelector('.brand_comp').innerText = data[0].brand; // Assuming the brand of the first item
        document.querySelector('.descri_comp').innerText = data[0].description; // Assuming the description of the first item
        document.querySelector('.compare_img').src = data[0].image_url; // Assuming the image_url of the first item
    },
    error: function (error) {
        console.error(error);
    }
});
