# Running the Project
1. Ensure that the [backend service](https://github.com/dvilinsky/testermatch) is up and running. 
2. Install [gradle](https://gradle.org/install/)
3. Run gradlew clean build. 
4. This will create the jar at `build/libs/testermatch-client-1.0-SNAPSHOT.jar`. Run this jar with
`java -jar build/libs/testermatch-client-1.0-SNAPSHOT.jar`
5. Client will be accessible on the command line. 

# Discussion
## Using the client
This client operates as a simple REPL. When you run the code, you will be presented with the following two statements:

`Enter a country or a comma-separated list of each country, or all for every country (enter to skip, \q to quit)
 `
 
 `country:`
 
 
 `Enter a device or a comma-separated list of each device, or all for every device (enter to skip, \q to quit)`
 
` device:`

A couple of notes on this:
1. When you hit enter, the code interprets this as meaning you do not intend to search by that field, and is equivalent to 
entering "all" for that field
2. The comma-separated list of values accepts a pretty broad range of input forms. So, queries like `GB,US` or `GB, US` or 
`gb,us` will work. Please do not include quotes around each country.

## Returned data
Provided you have inputted the data correctly, the REPL will return a table-like view with
three columns: First Name, Last Name, and Bugs. Bugs refers to the number of bugs this tester has identified on the given devices.
This list is sorted by *experience*, which directly corresponds to the number of bugs a tester has identified.

Note that if you incorrectly enter a country or device, for example "GBB" when you want "GB", the program does
not consider that an error. It would simply return the testers in that country, which in this case is none, because there are 
no testers in "GBB".

## Implementation details
Like the backend service, the client follows the same pattern of there being 
four possible cases for a user search:
1. Country=ALL and Device=All
2. Country=ALL and Device=[SOME DEVICE(s)]
3. Country=[SOME COUNTRY(s)] and Device=All
4. Country=[SOME COUNTRY(s)] and Device[SOME DEVICE(s)]

When a user enters a search, it is first validated that it is a properly-formatted comma-separated list. Once validated, 
it is sent to a `QueryExecutor` that routes the search to the correct endpoint, based on the 
cases described above. Then, the results are formatted by a `ResponseFormatter` and returned to 
the REPL loop for display to the user.

## Areas for improvement
As with the backend, the client suffers from the same extensibilty problem noted in that readme. In the 
`QueryExecutor`, there is an `if` block which performs the routing. As the number of search dimensions 
grows, this `if` block grows exponentially. 

Other areas for improvement include the validation regex in `src/main/java/Validator`. While it works, it could probably be tighter
and accept less input than it currently does. 