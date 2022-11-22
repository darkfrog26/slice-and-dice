# Let's Slice & Dice
## Take home test for Clipboard Staff Software Engineer position

# TODO
- [ ] Create Database model
- [ ] Create Security model
  - [ ] Create a dummy user
- [ ] Create API
  - [ ] Add a new record
  - [ ] Delete a record
  - [ ] Fetch SS for salary over the entire dataset
  - [ ] Fetch SS for salary on_contract
  - [ ] Fetch SS for salary for each department
  - [ ] Fetch SS for salary for each department and sub-department
  - [ ] Add login to get token
  - [ ] Add token auth to each API end-point
- [ ] Tests for each API end-point
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