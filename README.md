# LocalConnect-API4J
Java implementation of Local Connect API.

## Features
Refer [LocalConnect-Doc](https://github.com/LocalConnect-Dev/LocalConnect-Doc) .

## Environment
- Java Runtime Environment 10
- Maven 3

## Installation
1. Clone this repository.
2. Create `local_connect.properties` and set variables.
3. Execute `mvn package`
4. Execute `java -jar target/api-<version>.jar`

## Configuration
```properties
SQL.Type = // SQL Type (usually `mysql`)
SQL.Host = // SQL Hostname
SQL.Port = // SQL Port Number
SQL.User = // SQL Username
SQL.Password = // SQL Password
SQL.Database = // SQL Database/Schema Name
Http.Port = // HTTP serving port
WebSocket.KeepAlive = // Interval of sending keep-alive signal
```

## License
Copyright (C) 2018 Hitoki Minami, Takashi Sakaguchi, Shuya Kawai, Naoki Ikeguchi, Kanato Shibata, Keita Tsuzuki.  
  
This project is released under the **GNU General Public License 3.0** .  
For details, refer `LICENSE.md` .
