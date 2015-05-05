# templar-service
High performance Json Rest API for GeoIP and Grok named-regex.
It was written in scala using akka and spray technology.

## Usage
Start service by issu command line as follow.

    TEMPLAR-SERVICE_HOME/bin/templar-service [-options]

Available option :-

          --debug         Print debugging information
      -l, --log <arg>     Enable log to file, default is
                          SERVICE_HOME/logs/templar-service.log
      -p, --port <arg>    Service port, default is 10000
          --silent        Omit messages to console
          --help          Show this message
          --version       Print version

## All Services
templar-service has two services - Grok and GeoIP services.
Support both POST and GET methods.

### Grok Service
Used for extracting data from defined pattern. You must compile pattern first and then
extract data from pattern id. URL as follow.

    http://hostname:port/grok/command?????

Pattern and Data may be very complex. Recommends to use POST method.

#### Compile Pattern

    http://hostname:port/grok/compile
   
Content must written in json format like this.

    { "pattern" : "GROK PATTERN STRING" }
     
If it's ok, it will return pattern id.

#### Extract Data

    http://hostname:port/grok/extract

Content must written in json format like this.

    { "patternId" : patternId, "data" : "ANY DATA" }

### GeIP Service

    http://hostname:port/geoip/command??????

Used for get location information from ip address. We used GeoLite2 databases and api from MaxMind.

Available commands :-

    info            ip=ip address
    country         ip=ip address
    region          ip=ip address
    city            ip=ip address
    organization    ip=ip address
    location        ip=ip address
    distance        from=ip address, to=ip address

## Examples
In examples, we will use **curl** utility.

#### Add pattern

    curl -XPOST 'http://localhost:10000/grok/compile' -H "Content-Type: application/json" 
    -d '{ "pattern" : "%{IPORHOST:clientip} %{USER:ident} %{USER:auth} \\[%{HTTPDATE:access_datetime}\\] \"(?:%{WORD:verb} %{NOTSPACE:request}(?: HTTP/%{NUMBER:httpversion})?|%{DATA:rawrequest})\" %{NUMBER:response} (?:%{NUMBER:bytes}|-) %{QS:referrer} %{QS:agent}" }'

#### Extract data
    
    curl -XPOST 'http://localhost:10000/grok/extract' -H "Content-Type: application/json" 
    -d '{ "patternId" : 1,
          "data" : "111.73.45.49 - - [24/Oct/2013:14:03:32 +0700] "GET /firstcore/viewcourse.jsp?courseId=154+++++++Result:+chosen+nickname+%22Essepsciedo%22;+success;+Result:+chosen+nickname+%22tisteteEbra%22;+success; HTTP/1.0" 200 26473 "http://www.cdgm.co.th/firstcore/viewcourse.jsp?courseId=154+++++++Result:+chosen+nickname+%22Essepsciedo%22;+success;+Result:+chosen+nickname+%22tisteteEbra%22;+success;" "Opera/9.80 (Windows NT 5.1; U; ru) Presto/2.10.289 Version/12.00" }'

#### GeoIP POST

    curl -XPOST 'http://localhost:10000/geoip/info' -H "Content-Type: application/json" -d '{ "ipAddress" : "111.73.45.49" }'
    curl -XPOST 'http://localhost:10000/geoip/country' -H "Content-Type: application/json" -d '{ "ipAddress" : "111.73.45.49" }'
    curl -XPOST 'http://localhost:10000/geoip/region' -H "Content-Type: application/json" -d '{ "ipAddress" : "111.73.45.49" }'
    curl -XPOST 'http://localhost:10000/geoip/city' -H "Content-Type: application/json" -d '{ "ipAddress" : "111.73.45.49" }'
    curl -XPOST 'http://localhost:10000/geoip/organization' -H "Content-Type: application/json" -d '{ "ipAddress" : "111.73.45.49" }'
    curl -XPOST 'http://localhost:10000/geoip/location' -H "Content-Type: application/json" -d '{ "ipAddress" : "111.73.45.49" }'
    curl -XPOST 'http://localhost:10000/geoip/info' -H "Content-Type: application/json" -d '{ "ipAddress" : "66.249.77.31" }'
    curl -XPOST 'http://localhost:10000/geoip/distance' -H "Content-Type: application/json" -d '{ "fromIp" : "66.249.77.31", "toIp" : "111.73.45.49" }'

#### GeoIP GET

    curl -XGET 'http://localhost:10000/geoip/info?ip=111.73.45.49'
    curl -XGET 'http://localhost:10000/geoip/country?ip=111.73.45.49'
    curl -XGET 'http://localhost:10000/geoip/region?ip=111.73.45.49'
    curl -XGET 'http://localhost:10000/geoip/city?ip=111.73.45.49'
    curl -XGET 'http://localhost:10000/geoip/organization?ip=111.73.45.49'
    curl -XGET 'http://localhost:10000/geoip/location?ip=111.73.45.49'
    curl -XGET 'http://localhost:10000/geoip/distance?from=111.73.45.49&to=66.249.77.31'

##### Notes :-
This product includes GeoLite2 data created by MaxMind, available from
<a href="http://www.maxmind.com">http://www.maxmind.com</a>.
