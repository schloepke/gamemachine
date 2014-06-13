module GameMachine

  module MessageLib
    include_package 'GameMachine.Messages'
  end

  module RedisLib
    include_package 'redis.clients.jedis'
    include_package 'redis.clients.jedis.exceptions'
    include_package 'redis.clients.util'
  end

  module ZeromqLib
    include_package 'akka.zeromq'
  end

  module ProtoLib
    include_package 'com.dyuproject.protostuff'
  end

  module JavaLib
    java_import 'akka.cluster.ClusterEvent'
    include_package 'akka.serialization'
    include_package 'com.typesafe.config'
    include_package 'akka.actor'
    include_package 'akka.io'
    include_package 'akka.util'
    include_package 'akka.pattern'
    include_package 'akka.testkit'
    include_package 'akka.cluster'
    include_package 'akka.routing'
    include_package 'akka.serialization'
    include_package 'akka.contrib.pattern'
    include_package 'akka.event'
    include_package 'akka.camel.javaapi'
    include_package 'akka.camel'
    include_package 'com.game_machine.core'
    include_package 'java.net'
    include_package 'java.util.concurrent.atomic'
    include_package 'scala.concurrent.duration'
    include_package 'scala.concurrent'
    include_package 'java.io.Serializable'
    include_package 'java.util'
    include_package 'java.io'
    include_package 'com.couchbase.client'
    include_package 'io.netty.channel'
  end
end
