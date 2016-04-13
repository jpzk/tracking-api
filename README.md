Requirements: Linux host with git and docker

## Installation

<pre>
  git clone git@github.com:jpzk/tracking-service.git
  cd tracking-service 

  docker build -t trackingservice .
  docker run -d --name trackingservicec -v /var/data/tracking:/var/data/tracking -p 8888:8888 -t trackingservice 
</pre>

