require 'riak'
require 'json'
require 'benchmark'

def token(length=16)
  chars = [*('A'..'Z'), *('a'..'z'), *(0..9)]
  (0..length).map {chars.sample}.join
end
  
client = Riak::Client.new(:nodes => [
    {:host => 'localhost', :http_port => 8091},
    {:host => 'localhost', :http_port => 8092},
    {:host => 'localhost', :http_port => 8093},
    {:host => 'localhost', :http_port => 8094}
  
  ])

bucket = client.bucket("doc") 

id = 1000000000000000
data = token(50000)
data = JSON.generate({:data => data})

1000000.times do
  object = bucket.get_or_new(id)
  object.raw_data = data
  #object.content_type = "application/x-ruby-marshal"
  object.content_type = "application/json"
  object.store
  id += 1
end