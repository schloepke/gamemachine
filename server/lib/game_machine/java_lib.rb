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

  module TutorialLib
    include_package 'com.game_machine.examples'
  end

  module ModelLib
    include_package 'com.game_machine.orm.models'
  end
  
  module JavaLib
    java_import 'com.typesafe.config.Config'
    java_import 'com.typesafe.config.ConfigFactory'
    java_import 'akka.cluster.ClusterEvent'
    java_import 'akka.contrib.pattern.ClusterSingletonManager'
    java_import 'akka.contrib.pattern.ClusterSingletonProxy'
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
    java_import 'com.game_machine.core.Grid'
    include_package 'com.game_machine.core'
    include_package 'com.game_machine.codeblocks'
    include_package 'com.game_machine.orm'
    include_package 'com.game_machine.authentication'
    include_package 'com.game_machine.ui'
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
