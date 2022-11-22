# Let's Slice & Dice
## Take home test for Clipboard Staff Software Engineer position

http://localhost:8080/user/authenticate?username=clipboard&password=clipboard
- Local token: 4xvnYE6dCNNWy2ZyQEQW8KvREJhhQpvO

# TODO
- [X] Create Database model
- [X] Create Security model
  - [X] Create a dummy user
- [X] Create preliminary web server
- [X] Create API
  - [X] Add a new record
  - [X] Delete a record
  - [X] Fetch SS for salary over the entire dataset
  - [X] Fetch SS for salary on_contract
  - [X] Fetch SS for salary for each department
  - [X] Fetch SS for salary for each department and sub-department
  - [X] Add login to get token
  - [X] Add token auth to each API end-point
- [X] Tests for each API end-point
- [ ] Add GitHub Action for CI
- [ ] Create Dockerized deployment
- [ ] Create docker-compose file to start ArangoDB and server
    - docker run --name arangodb -p 8529:8529 -d --rm -it -e ARANGO_NO_AUTH=1 arangodb/arangodb-preview:3.10-nightly
- [ ] Create bundling script (Create Docker and publish it locally)
- [ ] Update README
  - [ ] Explain each library dependency
  - [ ] Explain database choice
  - [ ] Explain how to run
  - [ ] Add API call docs
  - [ ] Remove TODO
- [ ] Bundle up and release