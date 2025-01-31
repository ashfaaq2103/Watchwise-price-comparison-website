const chai = require("chai");
const chaiHttp = require("chai-http");
const server = require("../server"); // Adjust the path based on your project structure

const should = chai.should();

chai.use(chaiHttp);

// Test suite for the server
describe("server", function () {
    // Test for the GET /watch endpoint
    describe("GET /watch", () => {
        it("should return status 200 and an object with total watch items and an array of watches", (done) => {
            chai
                .request(server)
                .get("/watch")
                .end((err, res) => {
                    res.should.have.status(200);
                    res.body.should.be.an("object");
                    res.body.should.have.property("totNumItems");
                    res.body.should.have.property("data");
                    res.body.data.should.be.an("array");
                    done();
                });
        });
    });

    // Test for the GET /watch/search endpoint
    describe("GET /watch/search", () => {
        it("should return status 200 and an array of watches matching the search term", (done) => {
            const searchTerm = "Seiko";

            chai
                .request(server)
                .get(`/watch/search?description=${searchTerm}`)
                .end((err, res) => {
                    res.should.have.status(200);
                    res.body.should.be.an("array");

                    if (res.body.length > 0) {
                        res.body.forEach((watch) => {
                            // Check for properties of each watch in the array
                            watch.should.have.property("watch_id");
                            watch.should.have.property("prod_name");
                            watch.should.have.property("brand");
                            watch.should.have.property("description");
                            watch.should.have.property("image_url");
                        });
                    }

                    done();
                });
        });
    });

    // Test for the GET /watch/{watch_id} endpoint
    describe("GET /watch/{watch_id}", () => {
        it("should return status 200 and details for the specified watch", (done) => {
            const watchId = 5;

            chai
                .request(server)
                .get(`/watch/${watchId}`)
                .end((err, res) => {
                    res.should.have.status(200);
                    res.body.should.be.an("array");

                    if (res.body.length > 0) {
                        const watch = res.body[0];
                        // Check for properties of the retrieved watch
                        watch.should.have.property("brand");
                        watch.should.have.property("image_url");
                        watch.should.have.property("description");
                        watch.should.have.property("url");
                        watch.should.have.property("price");
                        watch.should.have.property("website");
                    }

                    done();
                });
        });
    });

    // Test for an invalid path
    describe("GET Invalid Path", () => {
        it("should return status 404 for an invalid path", (done) => {
            chai
                .request(server)
                .get("/invalidpath")
                .end((err, res) => {
                    res.should.have.status(404);
                    done();
                });
        });
    });

    // Test for the GET /watch with Pagination endpoint
    describe("GET /watch with Pagination", () => {
        it("should return status 200 and paginated array of watches", (done) => {
            const numItems = 29; // Replace with a valid number
            const offset = 0; // Replace with a valid offset

            chai
                .request(server)
                .get(`/watch?num_items=${numItems}&offset=${offset}`)
                .end((err, res) => {
                    res.should.have.status(200);
                    res.body.should.be.an("object");
                    res.body.should.have.property("totNumItems");
                    res.body.should.have.property("data");
                    res.body.data.should.be.an("array");
                    done();
                });
        });
    });
});
