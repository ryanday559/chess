# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Phase 2 Sequence Diagram

[![Sequence Diagram](Map Progress.svg)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6mgfAgEGSRCpHZUbs8YApTShgOb2ur0ABRS0qbAEApe26le7Fcz1QJOYLDcZzdTARkLZaRqDeOoGqZK43B0Py+Rq9BQsycTB2vnqX1Vb0oepvHmJin3Vt01S1ECq9FSqDsgY87nae3t+7GWoKDgcTXS7T9n2D+dtkdjkPohQ+PUY4Cn+Kzlt74eC5er9cnvV9xE7+4wp5l7FovFqd1YJ+cIdv6ZavIaSpLPU+wgheertBA9ZoFByyXImlAdqmGD1OEThONmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwJG0hwAoMAADIQFkhRYcwTrUAGzRtF0vQGOo+RoNmipzGsvz-BwVygZSQEBuBFafJCIJqTsXzQo8wHiVQSIwAgQnihignCQSRJgKSb6GLuNL7gyTKTtO+lcjeXl3kuwowGKEqTjKcrlu8SqYCqIYapORocBAagwGgEDMFaaIwOKRjaHoBhBbyIWWdZboetuHmWQGKVKtI6WqAActl0ZotGsbxoUWnJpUon1AArHhBG5qo+bzNBxalvUDVzJl2UwCABTyOKKDrjqeoFW8hXyMVKAAISNvRpULqo-XuXNM5bkODqhfUcDxCgIAak0+h-DssryspYjuYKV3Lc9r3vVsOwYsZAJrFF2ikol6owAAkmgK0li9zCg59AJnfuvqA45BWvs6KCXdUAbI6jKLgJj6ldSgcYKehUD9YNMAZgAjGN-KTXsM3QD20yXtASAAF4bSdzbuRUd30st44oM+8Tnpe14yxd5TLoGa6BirW5SxUOllgTaAZKoAGYIbpMSWBhEBV8sGXlRDaQppZPMxUrO4fhoy23FBkwesH3qaZTYMZ43h+P4XgoOgMRxIk0ex45vhYKJANu-UDTSJG-GRu0kbdD0cmqApwwUU7vVuwb5nPOM5eIc7tEWzXzPlIDtn2CnDlCSnzlqK5NXS7e-I+WAivK-BDdoHOwX8hUmtPS9Gr10hxrgEgND5Wge26PoyqqhqFPQFTzAr+gOPDlbVkupl7qevrbcZ0jKPH+jisV3TDMJqBLPIGmbNOE5j7caPNpoln5gqQWephZi12HRZsasr7WUVkTLsQ9Z6y2kCgbgx5LwT0olPGeZU54azChQU+jsp4wEgLfD0W8d4HUHtXWEAZk6nlNubS2FRQIvFdtbX+JQwA4VGqMeBYcmKRxROufw2BxQan4nlAA4kqDQac6plgaIovOhd7BKjLpQpCTNmFflqHXAxjc0KW0fl2eoyAcjKNzBiOxYAHFqD7iSQe5Q1aj3HmfaeF8SEL2Bsvcx29QAEE3gVLQ+094JQPs-Sm6NjShICeoJBN8qr32JlfcmL80bgHflPT+PUjEez-thABQCczcwLGA2akCKIwPFmI1J6t3IYhQbDTx3iYCMhcSojERDzrzzCoopkVYEBrwiYYXRuZ6HRN0CVRB3Dib1HCRvFAriNAP0Nms9eNAtnNxYe7axNRM7jFmSgRG0hCzs3CMEQIIJNjxF1CgeaU0A5jGSKANU7z7bLEuS1JULtOilPKJ7ERDQLlKmubc+5jzljPNeX8wyyxvmvRRZ8wFwLaKgtOuHZi-gOAAHY3BOBQE4GIkZghwC4gANngPLGAriYBFHKWJP0T8pIdB0XoqBBCkLZmxXMPhNRjFwlMWMPx-yxjCo+ZYluKybFyyPJspUGJDxyDVXMdxA8pZeOHrLPpvjQlDP3CMx6wTkmT1XusyJ28FmMPhofPJJ9rUCvPss05lU75MNObkxJBTQnFMZj-Mpgj0yAK5nmWpRZwFlh8PyppcDQ6tPSd2HWL49bZINRgkcKqtWuIxHKs1l9SGPSZWgFAmxemViiUVPe2oei1oWpyNNSrr4Zsudcv1uzmWNS3Fw-1Ntu03PqHch5orKDhv-l7IVMKx0wAnYEFpBLI6WGwbZGtsQkAJDABuj0EAa0ACkIAFRZf4dFao2WCPTtbTOTRmQyR6Jc-RNr0DZmwAgYAG6oBwAgLZKAaxR1Ttbn28YX6f2UH-YBvYAB1FgiN849AAEL8QUHAAA0t8UdcLJ1HK-B26yAArM9aAi2kfFK43Vbkc09ONXgvxpbAlhUXq9d1FcpkbPmQ2kqzqEmv3ABxqe7bvUZN9Q-HhAn8lj2DTGemJSw3gvZZGqpYwQGxrGHzBNSaoCi2aam-V9GmRFtHcxtJ5b4BWrtTMutDreP7yStJt1o7ROAxZagztOSyxHxk6465Ibv5VxnRUka3tqkxqmnG+pWUW2GC2vqM22Av2oHXLhlp+rAYgGCU0b9v6YPQAUFQcEuhuAQzy9BgD0A4bxMg-lqrsAgxmnlOGQxkmn5NZDGGCMaBAuV34SFoRACszAJqVFrT8a1nVnNNQnrEsGJevcwO+Q3TDX5r8NElAuWoN-oa+VnbBWgP9rmLOVliDLOPhgHVjCNDLnGgFDYF6SAABmqWrsVd24BzxfbT1UaVBwhAgFFXDt4UY5TEaYBztEamtdAQvA-rjruhOUAEeIBDLAYAyXkAgDyAUVlajOX3saNnXO+dC7GDB+BsyxyiM3xANwPACgseEFx2gDVDOoBM5SzjnqNHVt5vqPT9HXPses8GWmyzptxmOr3i4eAHOJmzeSGoFw32W6PQ5yLlnvP-yA4I3CDtulQODeEeFq4qagA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
