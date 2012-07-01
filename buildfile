
repositories.remote << 'http://repo2.maven.org/maven2'
repositories.remote << 'http://www.ibiblio.org/maven2'

define 'game_machine' do
  project.version = '0.1.0'
  compile.with transitive('io.netty:netty:jar:3.5.1.Final')
  compile.with transitive('com.google.protobuf:protobuf-java:jar:2.4.1')
  package :jar
end