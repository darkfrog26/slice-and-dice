# Let's Slice & Dice
## Take home test for Clipboard Staff Software Engineer position
[![CI](https://github.com/darkfrog26/slice-and-dice/actions/workflows/ci.yml/badge.svg)](https://github.com/darkfrog26/slice-and-dice/actions/workflows/ci.yml)
I've gone a bit overboard to create a solution to the take home test, but if I were really going to release this to the
public, there's quite a lot of improvements I would make.

# Language Choice
My language of choice is generally Scala, though I've been delving deeper into Rust recently. However, because of my
vast knowledge of the language and custom libraries I've written, creating the solution using Scala was a clear choice.

# Database Choice
The test PDF suggests an in-memory database, but I chose to use ArangoDB instead because I believe it better reflects
the kind of solution I would actually write for a production scenario, and I just really like the database.

# Dependencies
If you take a look at the build.sbt file, you'll see several third-party dependencies referenced and I'd like to take a
moment to outline them, their purpose, and why I chose them:
- Scribe (https://github.com/outr/scribe): The fastest logging library on the JVM. I'm a bit biased because I wrote it,
but take a look at the benchmarks if you're curious. It's also incredibly powerful.
- Scarango (https://github.com/outr/scarango): My choice for connecting to the database. Yes, I also wrote it, and it's
the defacto standard Scala driver for ArangoDB. It provides a lot of infrastructure to write great code working with the
database.
- Fabric (https://github.com/typelevel/fabric): Though not explicitly depended on, it is used behind the scenes for JSON
parsing and formatting. Yep, I also wrote it.
- Spice (https://github.com/outr/spice): I considered using http4s, but the past few times I've used it on teams, the
complexity really confused the more junior developers on my team. Spice is something of a prototype web server I created
to simplify client / server communication. One of the things that's a bit unique about Spice is the `Restful` trait. It
gives the API the ability to be called via POST or GET parameters for easier local testing.

# Getting Started
There are a few basic requirements to get set up:
- SBT (https://www.scala-sbt.org): This is the Scala Build Tool used to build the code
- Docker and Docker Compose: Since the requirements for this code assignment specifically call for this, I don't think
additional explanation is necessary.

## Building and Publishing Locally
In order to build and publish the Docker image locally to be used in the `docker-compose.yml` file, you'll need to run
the `publish-local.sh` script file in the root of the project.

## Starting
Starting both the web server and ArangoDB should be as easy as running `docker compose up` after you've successfully
built and locally published the image.

## Verifying
You can connect to the local ArangoDB instance by opening http://localhost:8529 in the browser. In addition, you can
test the authentication in the browser via: http://localhost:8080/user/authenticate?username=clipboard&password=clipboard

## Testing
You can run the `test.sh` script to run the unit tests locally. However, this assumes a running instance of ArangoDB to
test against. Notice in the spec that all points of the criteria is covered (https://github.com/darkfrog26/slice-and-dice/blob/master/src/test/scala/spec/APISpec.scala).

## Endpoints
Unfortunately, due to lack of time, I wasn't able to create an OpenAPI / Swagger implementation for this, but you can
see the endpoints in action in the spec tests. You should also be able to see the tests successfully running in the
GitHub Action CI.

These are the exposed end-points you'll find at localhost:8080:
- `/dataset/create`: Creates the employee entry
- `/dataset/delete`: Deletes the employee entry by name
- `/dataset/statistics`: Returns the summary statistics against all employees
- `/dataset/statistics/department`: Returns the summary statistics grouped by department
- `/dataset/statistics/subdepartment`: Returns the summary statistics grouped by department and sub-grouped by sub-department
- `/user/authenticate`: Authenticates a user login. There is a hard-coded user `clipboard` / `clipboard` created when the database is created
- `/user/logout`: Removes the authentication token entry from the database for a specific login token.