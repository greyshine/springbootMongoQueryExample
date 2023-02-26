docker run \
  --name test-mongodb \
  -d \
  -p 27017:27017 \
  -v $(pwd)/mongodb-data:/data/db \
  mongo:6.0.3