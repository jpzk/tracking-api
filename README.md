Requirements: Linux host with git and docker

## Installation

<pre>
  git clone git@github.com:jpzk/tracking-service.git
  cd tracking-service 

  docker build -t trackingservice .
  docker run -i --name trackingservicec -p 80:8888 -p 81:9990 -t trackingservice "/bin/bash"
</pre>

The API endpoint is served on container port 8888 and on the host system it is 80.
The API endpoint is served on container port 9990 and on the host system it is 81.

## Usage

Now we only need to start the finatra service by

<pre>
java -jar /trackingservice/tracking-api.jar &
</pre>

Use the client in example-cpuusage-api/client/generator.py to send requests to API which runs on port 80.


