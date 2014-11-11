module GameMachine

  module MessageLib
    include_package 'io.gamemachine.messages'
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
    include_package 'io.protostuff'
  end

  module ApiLib
    include_package 'io.gamemachine.api'
  end

  module DbLib
    include_package 'io.gamemachine.objectdb'
  end

  module TutorialLib
    include_package 'io.gamemachine.examples'
  end

  module ModelLib
    include_package 'io.gamemachine.orm.models'
  end
  
  module ClientLib
    include_package 'io.gamemachine.client.api'
    include_package 'io.gamemachine.client.api.cloud'
    include_package 'io.gamemachine.client'
  end

  module NetLib
    include_package 'io.gamemachine.net.udp'
    include_package 'io.gamemachine.net.tcp'
    include_package 'io.gamemachine.net.http'
  end

  module JavaLib
    java_import 'com.typesafe.config.Config'
    java_import 'com.typesafe.config.ConfigFactory'
    java_import 'akka.cluster.ClusterEvent'
    java_import 'akka.contrib.pattern.ClusterSingletonManager'
    java_import 'akka.contrib.pattern.ClusterSingletonProxy'
    include_package 'io.gamemachine.routing'
    include_package 'io.gamemachine.config'
     include_package 'io.gamemachine.util'
    include_package 'akka.serialization'
    include_package 'com.typesafe.config'
    include_package 'akka.actor'
    include_package 'akka.io'
    include_package 'akka.testkit'
    include_package 'akka.cluster'
    include_package 'akka.routing'
    include_package 'akka.serialization'
    include_package 'akka.contrib.pattern'
    include_package 'akka.event'
    include_package 'akka.camel.javaapi'
    include_package 'akka.camel'
    java_import 'io.gamemachine.core.Grid'
    include_package 'io.gamemachine.core'
    include_package 'io.gamemachine.net'
    include_package 'io.gamemachine.codeblocks'
    include_package 'io.gamemachine.shared.codeblocks'
    include_package 'io.gamemachine.orm'
    include_package 'io.gamemachine.authentication'
    include_package 'io.gamemachine.ui'
    include_package 'java.net'
    include_package 'java.util.concurrent.atomic'
    include_package 'scala.concurrent.duration'
    include_package 'java.io.Serializable'
    include_package 'java.util'
    include_package 'java.io'
    include_package 'com.couchbase.client'
    include_package 'com.google.common.cache'
    #include_package 'io.netty.channel'
  end
end
